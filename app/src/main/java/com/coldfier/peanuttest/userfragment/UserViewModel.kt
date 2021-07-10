package com.coldfier.peanuttest.userfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coldfier.peanuttest.repository.AccountInformation
import com.coldfier.peanuttest.repository.retrofit.ApiClient
import com.coldfier.peanuttest.repository.PeanutUserWithToken
import com.coldfier.peanuttest.repository.UserData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(json: String) : ViewModel() {

    private val userData: UserData

    private var _accountInformation = MutableLiveData(AccountInformation())
    val accountInformation: LiveData<AccountInformation>
        get() = _accountInformation

    private var _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    init {
        userData = initUser(json)
        getAccountInformation(userData)
    }

    private fun initUser(json: String): UserData {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(UserData::class.java)
        val userData = jsonAdapter.fromJson(json)

        return userData!!
    }

    private fun getAccountInformation(userData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiService = ApiClient.getPeanutApiService()
            val peanutUserWithToken = PeanutUserWithToken(userData.login, userData.peanutToken)
            apiService.getAccountInformation(
                peanutUserWithToken
            ).enqueue(object : Callback<AccountInformation> {
                override fun onResponse(
                    call: Call<AccountInformation>,
                    response: Response<AccountInformation>
                ) {
                    if (response.isSuccessful) {
                        val accountInformation = response.body()
                        _accountInformation.postValue(accountInformation!!)
                    }
                }

                override fun onFailure(call: Call<AccountInformation>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

            apiService.getLastFourNumbersPhone(peanutUserWithToken).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val number = response.body()?.string()
                        val correctNumber = number?.substring(1, number.lastIndex)
                        _phoneNumber.postValue(correctNumber)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}