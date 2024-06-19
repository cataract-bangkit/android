package com.cataract.detection.dashboard

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cataract.detection.R
import com.cataract.detection.connection.model.DetectionModel
import com.cataract.detection.databinding.FragmentDetectionBinding
import com.cataract.detection.viewmodel.DetectionViewModel
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class DetectionFragment : Fragment() {
    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private var croppedImageUri: Uri? = null

    private var resultPrediction: DetectionModel.Result? = null

    private val CAMERA_PERMISSION_CODE  = 101
    private val STORAGE_PERMISSION_CODE = 102

    private val detectionViewModel: DetectionViewModel by viewModels()

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    startUCrop(uri)
                }
            }
        }

    private val launcherIntentCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap: Bitmap = result.data?.extras?.get("data") as Bitmap
            val tempUri = saveBitmapToTempFile(imageBitmap)
            startUCrop(tempUri)
        } else {
            showToast("Canceled")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galery.setOnClickListener {
            startGallery()
        }

        binding.camera.setOnClickListener {
            startCamera()
        }

        // Flag untuk mengatur apakah tombol sudah diklik atau belum
        var isProcessing = false

        binding.deteksi.setOnClickListener {
            if (!isProcessing) {
                isProcessing = true

                loading(true)

                croppedImageUri?.let {
                    showToast("Memulai Prediksi")
                    partImage(it)
                }

                detectionViewModel.resultPrediction.observe(requireActivity(), Observer { message ->
                    message?.let {
                        showToast("Berhasil Diprediksi, Pindah Ke Result")

                        resultPrediction = it

                        val bundle = Bundle()
                        bundle.putString("image", resultPrediction?.imgUrl.toString())
                        bundle.putString("result", resultPrediction?.result.toString())
                        bundle.putString("persen", resultPrediction?.confidence.toString())

                        findNavController().navigate(R.id.action_detectionFragment_to_resultDetectionFragment, bundle)

                        loading(false)
                        isProcessing = false  // Mengatur flag bahwa proses selesai
                    }
                })
            } else {
                showToast("Proses Sedang Berlangsung")
            }
        }


        detectionViewModel.messageError.observe(requireActivity(), Observer{ message ->
            message?.let {
                showToast(it)
            }
        })

        detectionViewModel.messageSuccess.observe(requireActivity(), Observer{ message ->
            message?.let {
                showToast(it)
            }
        })


    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCameraIntent()
        } else {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Needed")
                .setMessage("Camera permission is needed to take pictures")
                .setPositiveButton("OK") { _, _ ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                showToast("Permission Denied")
            }
        }
    }

    private fun startCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            launcherIntentCamera.launch(intent)
        } else {
            showToast("No camera app found")
        }
    }

    private fun saveBitmapToTempFile(bitmap: Bitmap): Uri {
        val timestamp = System.currentTimeMillis().toString()
        val tempFileName = "temp_$timestamp.jpg"
        val tempFile = File.createTempFile(tempFileName, null, requireContext().cacheDir)
        val outputStream = FileOutputStream(tempFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()

        return Uri.fromFile(tempFile)
    }

    private fun startUCrop(sourceUri: Uri) {
        val destinationFileName = "UCrop_" + System.currentTimeMillis()
        val destinationUri = Uri.fromFile(File(requireActivity().cacheDir, "$destinationFileName.jpg"))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(16f, 9f)
            .withMaxResultSize(1280, 720)
            .withOptions(getUCropOptions())
            .start(requireActivity(), this)
    }

    private fun getUCropOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setCompressionQuality(100)
        options.setFreeStyleCropEnabled(true)
        options.setHideBottomControls(true)
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.withAspectRatio(16f, 9f)
        return options
    }


    private fun covertUriToByteArray(image: Uri): ByteArray? {
        var inputStream: InputStream? = null

        try {
            inputStream = requireContext().contentResolver.openInputStream(image)
            if (inputStream != null) {
                return inputStream.readBytes()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }

        return null
    }

    private fun partImage(image: Uri){
        var image = covertUriToByteArray(image)?.let { image(it, randomName() + ".${getFileExtension(requireContext(), image).toString()}", "image") }
        if (image != null) {
            detectionViewModel.prediction(
                requireContext(),
                image
            )
        }
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        return if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mimeType = context.contentResolver.getType(uri)
            MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        } else {
            val path = uri.path
            path?.substringAfterLast('.', null.toString())
        }
    }

    private fun image(image: ByteArray, filename: String, field: String): MultipartBody.Part {
        return MultipartBody.Part.createFormData(field, filename, image.toRequestBody("image/jpeg".toMediaType()))
    }

    private fun randomName(): String {
        val currentDateTime = LocalDateTime.now()
        val timestamp = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        val randomChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val randomString = (1..5).map { randomChars.random() }.joinToString("")
        val randomNumber = Random.nextInt(100, 1000) // Angka acak antara 100 dan 999
        return "bitlab_$timestamp$randomString$randomNumber"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UCrop.REQUEST_CROP && resultCode == Activity.RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            resultUri?.let {
                croppedImageUri = it
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = UCrop.getError(data!!)
            error?.message?.let {
                showToast(it)
            }
        }
    }

    private fun showImage(){
        croppedImageUri?.let {
            binding.imagePreview.setImageURI(it)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loading(codition: Boolean){
        if(codition){
            binding.loading.visibility = View.VISIBLE
        } else {
            binding.loading.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "DetectionFragment"
    }

}