package com.coldfier.peanuttest.repository.retrofit

import com.coldfier.peanuttest.repository.AccountInformation
import com.coldfier.peanuttest.repository.AuthRequest
import com.coldfier.peanuttest.repository.AuthResponse
import com.coldfier.peanuttest.repository.PeanutUserWithToken
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PeanutApiService {
    @POST("/api/ClientCabinetBasic/IsAccountCredentialsCorrect")
    fun authorize(@Body request: AuthRequest): Call<AuthResponse>

    @POST("/api/ClientCabinetBasic/GetAccountInformation")
    fun getAccountInformation(@Body peanutUserWithToken: PeanutUserWithToken): Call<AccountInformation>

    @POST("/api/ClientCabinetBasic/GetLastFourNumbersPhone")
    fun getLastFourNumbersPhone(@Body peanutUserWithToken: PeanutUserWithToken): Call<ResponseBody>
}