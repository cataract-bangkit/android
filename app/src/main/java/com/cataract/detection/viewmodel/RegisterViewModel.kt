package com.cataract.detection.viewmodel

import android.content.Context
import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cataract.detection.connection.endpoint.ApiEndpoint
import com.cataract.detection.connection.model.RegisterModel
import com.cataract.detection.connection.service.ApiAuthenticationService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {
    private val apiService = ApiAuthenticationService.retrofit.create(ApiEndpoint::class.java)

    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> get() = _messageError

    private val _messageSuccess = MutableLiveData<String>()
    val messageSuccess: LiveData<String> get() = _messageSuccess

    fun registerUser(
        context: Context,
        name: Editable,
        email: Editable,
        phone: Editable,
        password: Editable
    ){
        apiService.register(name.toString(), email.toString(), phone.toString(), password.toString()).enqueue(object :Callback<RegisterModel.Success> {
            override fun onResponse(call: Call<RegisterModel.Success>, response: Response<RegisterModel.Success>) {
                if (response.isSuccessful) {
                    val successResponse = response.body()
                    if (successResponse != null) {
                        _messageSuccess.postValue("User Created")
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
                        _messageError.postValue("Upload unsuccessful: Default error message")
                    }
                }
            }
            override fun onFailure(call: Call<RegisterModel.Success>, t: Throwable) {
                // Handle failure
                // If upload fails, handle it as an unsuccessful response with a default message
                _messageError.postValue("${t.toString()} Upload unsuccessful: Default error message")
            }
        })
    }


    companion object{
        private val TAG = "RegisterViewModel"
    }

}