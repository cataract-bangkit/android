package com.cataract.detection.dashboard

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cataract.detection.AuthenticationActivity
import com.cataract.detection.R
import com.cataract.detection.databinding.FragmentDetectionBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream

class DetectionFragment : Fragment() {
    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    private var croppedImageUri: Uri? = null

    private val CAMERA_PERMISSION_CODE  = 101
    private val STORAGE_PERMISSION_CODE = 102

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    Log.d(TAG, uri.toString())
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

        binding.deteksi.setOnClickListener {
            loading(true)
            Handler(Looper.getMainLooper()).postDelayed({

                val bundle = Bundle()
                bundle.putString("image", croppedImageUri.toString())

                findNavController().navigate(R.id.action_detectionFragment_to_resultDetectionFragment, bundle)
                loading(false)
            }, 2000)
        }
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
            .withAspectRatio(1f, 1f)
            .start(requireActivity(), this)
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