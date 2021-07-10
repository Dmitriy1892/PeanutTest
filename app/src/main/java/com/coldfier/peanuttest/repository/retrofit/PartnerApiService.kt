package com.coldfier.peanuttest.repository.retrofit

import com.coldfier.peanuttest.repository.AuthRequest
import com.coldfier.peanuttest.repository.QuoteInfoItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PartnerApiService{

    @POST("/api/Authentication/RequestMoblieCabinetApiToken")
    fun authorize(@Body request: AuthRequest): Call<ResponseBody>

    @GET("/clientmobile/GetAnalyticSignals/{login}")
    fun getQuotes(
        @Path("login")
        login: Int,

        @Query("tradingsystem")
        tradingSystem: Int = 3,

        @Query("pairs")
        pairs: String,

        @Query("from")
        from: Int,

        @Query("to")
        to: Int,

        @Header("passkey")
        token: String
    ): Call<List<QuoteInfoItem>>
}