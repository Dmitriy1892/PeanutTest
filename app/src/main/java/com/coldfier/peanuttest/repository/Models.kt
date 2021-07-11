package com.coldfier.peanuttest.repository

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class AuthRequest(
    @Json(name = "login")
    var login: Int,

    @Json(name = "password")
    var password: String
)

data class AuthResponse(
    @Json(name = "result")
    var result: Boolean = false,

    @Json(name = "token")
    var token: String = ""
)

@Entity(tableName = "user_data")
@JsonClass(generateAdapter = true)
data class UserData(

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @Json(name = "login")
    val login: Int,

    @Json(name = "password")
    val password: String,

    @Json(name = "peanutToken")
    var peanutToken: String = "",

    @Json(name = "partnerToken")
    var partnerToken: String = ""
)

data class PeanutUserWithToken(
    @Json(name = "login")
    var login: Int,

    @Json(name = "token")
    var token: String
)

data class AccountInformation(

    @Json(name = "extensionData")
    var extensionData: Any = Any(),

    @Json(name = "address")
    var address: String = "",

    @Json(name = "balance")
    var balance: Double = 0.0,

    @Json(name = "city")
    var city: String = "",

    @Json(name = "country")
    var country: String = "",

    @Json(name = "currency")
    var currency: Int = 0,

    @Json(name = "currentTradesCount")
    var currentTradesCount: Int = 0,

    @Json(name = "currentTradesVolume")
    var currentTradesVolume: Int = 0,

    @Json(name = "equity")
    var equity: Double = 0.0,

    @Json(name = "freeMargin")
    var freeMargin: Double = 0.0,

    @Json(name = "isAnyOpenTrades")
    var isAnyOpenTrades: Boolean = false,

    @Json(name = "isSwapFree")
    var isSwapFree: Boolean = false,

    @Json(name = "leverage")
    var leverage: Int = 0,

    @Json(name = "name")
    var name: String = "",

    @Json(name = "phone")
    var phone: String = "",

    @Json(name = "totalTradesCount")
    var totalTradesCount: Int = 0,

    @Json(name = "totalTradesVolume")
    var totalTradesVolume: Int = 0,

    @Json(name = "type")
    var type: Int = 0,

    @Json(name = "verificationLevel")
    var verificationLevel: Int = 0,

    @Json(name = "zipCode")
    var zipCode: String = ""
)

data class QuoteInfoItem(
    @Json(name = "Id")
    var id: Int,

    @Json(name = "ActualTime")
    var actualTime: Int,

    @Json(name = "Comment")
    var comment: String,

    @Json(name = "Pair")
    var pair: String,

    @Json(name = "Cmd")
    var cmd: Int,

    @Json(name = "TradingSystem")
    var tradingSystem: Int,

    @Json(name = "Period")
    var period: String,

    @Json(name = "Price")
    var price: Double,

    @Json(name = "Sl")
    var sl: Double,

    @Json(name = "Tp")
    var tp: Double
)