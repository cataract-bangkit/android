package com.cataract.detection.viewmodel

import android.content.Context
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.auth0.android.jwt.JWT
import com.cataract.detection.connection.endpoint.ApiEndpoint
import com.cataract.detection.connection.model.LoginModel
import com.cataract.detection.connection.model.UserModel
import com.cataract.detection.connection.service.ApiAuthenticationService
import com.cataract.detection.connection.service.PreferencesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val apiService = ApiAuthenticationService.retrofit.create(ApiEndpoint::class.java)

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> get() = _messageError

    private val _messageSuccess = MutableLiveData<String>()
    val messageSuccess: LiveData<String> get() = _messageSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(
        context: Context,
        email: Editable,
        phone: Editable,
        password: Editable
    ){
        _isLoading.postValue(true)
        apiService.login(email.toString(), phone.toString(), password.toString()).enqueue(object : Callback<LoginModel.Success> {
            override fun onResponse(call: Call<LoginModel.Success>, response: Response<LoginModel.Success>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    val successResponse = response.body()
                    if (successResponse != null) {
                        successResponse?.let {
                            saveSession(context, it)
                        }
                    } else {
                        _messageError.postValue("Success But Response Not Found")
                    }
                } else {
                    // Handle unsuccessful response
                    val errorBody = response.errorBody()?.string()
                    if (!errorBody.isNullOrEmpty()) {
                        try {
                            // Parse error response
                            // val errorResponse = Gson().fromJson(errorBody, RegisterModel.Error::class.java)
                            _messageError.postValue("Error: ${errorBody}")

                        } catch (e: Exception) {
                            _messageError.postValue("Error parsing error response: $e")
                        }
                    } else {
                        // If error body is empty, handle it as an unsuccessful response with a default message
                        _messageError.postValue("Request unsuccessful: Default error message")
                    }
                }
            }

            override fun onFailure(call: Call<LoginModel.Success>, t: Throwable) {
                // Handle failure
                // If upload fails, handle it as an unsuccessful response with a default message
                _isLoading.postValue(false)
                _messageError.postValue("${t.toString()} Request unsuccessful: Default error message")
            }
        })
    }

    fun saveSession(context: Context, sessionData: LoginModel.Success) {
        val result = PreferencesService(context).setToken(sessionData.data)

        if (!result) {
            _messageError.postValue("Ada Masalah Pada Saat Ingin Menyimpan User Login")
        }

        val token = sessionData.data?.token

        var resultDecode = token?.let {
            decodeJWT(it)
        }

        val resultName = PreferencesService(context).setName(resultDecode?.name.toString())

        if (resultName) {
            _messageSuccess.postValue("Berhasil Login")
        } else {
            _messageError.postValue("Anda Mendaftar Akun, Namun Tidak Memasukan Nama, ada masalah dengan Nama akun anda")
        }


    }

    fun decodeJWT(token: String): UserModel.Fillable? {
        return try {

            val jwt = JWT(token)

            val id          = jwt.getClaim("id").asString()
            val sub         = jwt.getClaim("sub").asString()
            val name        = jwt.getClaim("name").asString()
            val email       = jwt.getClaim("email").asString()
            val phone       = jwt.getClaim("phone").asString()
            val issuedAt    = jwt.issuedAt
            val expiresAt   = jwt.expiresAt

            UserModel.Fillable(
                id          = id,
                sub         = sub,
                name        = name,
                email       = email,
                phone       = phone,
                issuedAt    = issuedAt,
                expiresAt   = expiresAt
            )

        } catch (exception: Exception) {
            _messageError.postValue("Invalid JWT: ${exception.message}")
            null
        }
    }


    companion object{
        private val TAG = "LoginViewModel"
    }

}