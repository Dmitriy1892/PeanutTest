package com.coldfier.peanuttest.repository.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiClient {

    companion object {
        private const val BASE_PEANUT_URL = "https://peanut.ifxdb.com"
        private const val BASE_PARTNER_URL = "https://client-api.contentdatapro.com"

        private var PEANUT_API_SERVICE: PeanutApiService? = null
        private var PARTNER_API_SERVICE: PartnerApiService? = null

        fun getPeanutApiService(): PeanutApiService {
            return getApiService(PEANUT_API_SERVICE, BASE_PEANUT_URL, PeanutApiService::class.java)
        }

        fun getPartnerApiService(): PartnerApiService {
            return getApiService(PARTNER_API_SERVICE, BASE_PARTNER_URL, PartnerApiService::class.java)
        }

        val a = PeanutApiService::class.java

        private fun <T> getApiService(apiService: T?, baseUrl: String, apiClass: Class<T>): T {
            var bufferApiService = apiService
            if (bufferApiService == null) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder().protocols(mutableListOf(Protocol.HTTP_1_1)).addInterceptor(interceptor).build()

                //Moshi
                val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

                //Retrofit
                val retrofit: Retrofit by lazy {
                    Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .client(client)
                        .build()
                }
                bufferApiService = retrofit.create(apiClass)
            }
            return bufferApiService as T
        }
    }
}