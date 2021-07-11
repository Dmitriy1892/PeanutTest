package com.coldfier.peanuttest.repository

import android.content.Context
import com.coldfier.peanuttest.repository.retrofit.ApiClient
import com.coldfier.peanuttest.repository.room.AccountDatabase

class AppRepository private constructor(context: Context) {

    private val roomDao = AccountDatabase.getInstance(context).accountDao()

    suspend fun authorize(login: Int, password: String): Boolean {
        val peanutToken = authorizeInPeanut(login, password)
        val partnerToken = authorizeInPartner(login, password)
        val userData = UserData(
            login = login,
            password = password,
            peanutToken = peanutToken,
            partnerToken = partnerToken
        )
        roomDao.deleteAccount()
        roomDao.saveAccount(userData)
        return true
    }

    suspend fun saveAccount(userData: UserData) {
        roomDao.saveAccount(userData)
    }

    suspend fun getAccount(): UserData? {
        return roomDao.getAccount()

    }

    suspend fun deleteAccount() {
        roomDao.deleteAccount()
    }

    suspend fun updateAccount(userData: UserData): Boolean {
        return try {
            val peanutToken = authorizeInPeanut(userData.login, userData.password)
            val partnerToken = authorizeInPartner(userData.login, userData.password)
            val updatedUserData = UserData(
                login = userData.login,
                password = userData.password,
                peanutToken = peanutToken,
                partnerToken = partnerToken
            )
            roomDao.updateAccount(updatedUserData)
            true
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            false
        }
    }

    fun getAccountInfo(userData: UserData): AccountInformation? {
        val apiService = ApiClient.getPeanutApiService()
        val peanutUserWithToken = PeanutUserWithToken(userData.login, userData.peanutToken)
        val res = apiService.getAccountInformation(peanutUserWithToken).execute()
        val response = res.body() as AccountInformation

        return response!!
    }

    fun getAccountPhoneNumber(userData: UserData): String? {
        val apiService = ApiClient.getPeanutApiService()
        val peanutUserWithToken = PeanutUserWithToken(userData.login, userData.peanutToken)
        val res = apiService.getLastFourNumbersPhone(peanutUserWithToken).execute()
        val response = res.body()?.string()

        return response!!
    }

    fun getQuotesList(login: Int,
                      pairs: String,
                      from: Int,
                      to: Int,
                      token: String
    ): List<QuoteInfoItem> {
        val apiService = ApiClient.getPartnerApiService()
        val res = apiService
            .getQuotes(login = login, pairs = pairs, from = from, to = to, token = token)
            .execute()
        val body = res.body() as  List<QuoteInfoItem>

        return body!!
    }

    private fun authorizeInPeanut(login: Int, password: String): String{
        val peanutApiService = ApiClient.getPeanutApiService()
        val res = peanutApiService.authorize(AuthRequest(login, password)).execute()
        val response = res.body() as AuthResponse
        val result = response.token
        return result
    }

    private fun authorizeInPartner(login: Int, password: String): String {

        val partnerApiService = ApiClient.getPartnerApiService()
        val res = partnerApiService.authorize(AuthRequest(login, password)).execute()
        val response = res.body()?.string()
        val token = response?.lastIndex?.let { response.substring(1, it) }

        return token!!
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