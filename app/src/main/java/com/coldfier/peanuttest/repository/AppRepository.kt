package com.coldfier.peanuttest.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.coldfier.peanuttest.repository.retrofit.ApiClient
import com.coldfier.peanuttest.repository.room.AccountDatabase
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

class AppRepository private constructor(context: Context) {

    private val roomDao = AccountDatabase.getInstance(context).accountDao()

    suspend fun authorize(login: Int, password: String): Boolean {
        val peanutToken = authorizeInPeanut(login, password)
        val partnerToken = authorizeInPartner(login, password)
        val userData = UserData(
            login,
            password,
            peanutToken,
            partnerToken
        )
        roomDao.saveAccount(userData)
        return true
    }

    suspend fun saveAccount(userData: UserData) {
        roomDao.saveAccount(userData)
    }

    suspend fun getAccount(): UserData? {
        val account = roomDao.getAccount()
        return if (account != null) {
            updateAccount(account)
            roomDao.getAccount()
        } else null
    }

    suspend fun deleteAccount() {
        roomDao.deleteAccount()
    }

    private suspend fun updateAccount(userData: UserData): Boolean {
        return try {
            val peanutToken = authorizeInPeanut(userData.login, userData.password)
            val partnerToken = authorizeInPartner(userData.login, userData.password)
            val updatedUserData = UserData(
                userData.login,
                userData.password,
                peanutToken,
                partnerToken
            )
            roomDao.updateAccount(updatedUserData)
            true
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        }
    }

    private fun authorizeInPeanut(login: Int, password: String): String {

        var result: String? = null
        var failMessage = ""

        val peanutApiService = ApiClient.getPeanutApiService()
        peanutApiService.authorize(AuthRequest(login, password)).enqueue(object :
            Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val authResponse = response.body() as AuthResponse
                    if (authResponse.result) {
                        result = authResponse.token
                    } else {
                        failMessage = "Sign-in error, please check the login or password"
                    }
                } else {
                    failMessage = "Sign-in error, please check the login or password"
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                failMessage = "Sign-in error, please check the internet connection"
            }
        })
        if (result != null) {
            return result!!
        } else throw IllegalArgumentException(failMessage)
    }

    private fun authorizeInPartner(login: Int, password: String): String {

        var result: String? = null
        var failMessage = ""

        val partnerApiService = ApiClient.getPartnerApiService()
        partnerApiService.authorize(AuthRequest(login, password)).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.string()
                    val tok = token?.lastIndex?.let { token.substring(1, it) }
                    result = tok
                } else {
                    failMessage = "Sign-in error, please check the login or password"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                failMessage = "Sign-in error, please check the internet connection"
            }
        })
        if (result != null) {
            return result!!
        } else throw IllegalArgumentException(failMessage)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppRepository? = null

        fun getInstance(context: Context): AppRepository {
            return synchronized(this) {
                INSTANCE ?: kotlin.run {
                    INSTANCE = AppRepository(context)
                    INSTANCE!!
                }
            }
        }
    }
}