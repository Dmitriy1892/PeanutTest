package com.coldfier.peanuttest.signinfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coldfier.peanuttest.repository.retrofit.ApiClient
import com.coldfier.peanuttest.repository.AuthRequest
import com.coldfier.peanuttest.repository.AuthResponse
import com.coldfier.peanuttest.repository.UserData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInViewModel : ViewModel() {

    private var _peanutAuthToken = MutableLiveData<String>()
    val peanutAuthToken: LiveData<String>
        get() = _peanutAuthToken

    private var _partnerAuthToken = MutableLiveData<String>()
    val partnerAuthToken: LiveData<String>
        get() = _partnerAuthToken

    private var _isFailedSignIn = MutableLiveData<String>()
    val isFailedSignIn: LiveData<String>
        get() = _isFailedSignIn

    private lateinit var authRequest: AuthRequest

    fun signIn(login: Int, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val peanutApiService = ApiClient.getPeanutApiService()
            peanutApiService.authorize(AuthRequest(login, password)).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    if (response.isSuccessful) {
                        val authResponse = response.body() as AuthResponse
                        if (authResponse.result) {
                            _peanutAuthToken.postValue(authResponse.token)
                            authRequest = AuthRequest(login, password)
                        } else {
                            _isFailedSignIn.postValue("Sign-in error, please check the login or password")
                        }
                    } else {
                        _isFailedSignIn.postValue("Sign-in error, please check the login or password")
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    _isFailedSignIn.postValue("Sign-in error, please check the internet connection")
                }
            })


            val partnerApiService = ApiClient.getPartnerApiService()
            partnerApiService.authorize(AuthRequest(login, password)).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val token = response.body()?.string()
                        val tok = token?.lastIndex?.let { token.substring(1, it) }
                        _partnerAuthToken.postValue(tok!!)
                    } else {
                        _isFailedSignIn.postValue("Sign-in error, please check the login or password")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    _isFailedSignIn.postValue("Sign-in error, please check the internet connection")
                }
            })
        }
    }

    fun serializeToJson(userData: UserData): String {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(UserData::class.java)
        val json = jsonAdapter.toJson(userData)

        return json
    }

    fun getUserLoginAndPassword(): AuthRequest {
        return authRequest
    }

}