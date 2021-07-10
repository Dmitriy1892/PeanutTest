package com.coldfier.peanuttest

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.coldfier.peanuttest.repository.AccountInformation

@BindingAdapter("userBalance")
fun userBalance(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.balance.toString()
}

@BindingAdapter("userAddress")
fun userAddress(textView: TextView, accountInformation: AccountInformation) {
    textView.text = "${accountInformation.address}, ${accountInformation.city}, ${accountInformation.country}"
}

@BindingAdapter("userEquity")
fun userEquity(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.equity.toString()
}

@BindingAdapter("userFreeMargin")
fun userFreeMargin(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.freeMargin.toString()
}

@BindingAdapter("userAnyOpenTrades")
fun userAnyOpenTrades(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.isAnyOpenTrades.toString()
}

@BindingAdapter("userSwapStatus")
fun userSwapStatus(textView: TextView, accountInformation: AccountInformation) {
    textView.text = if (accountInformation.isSwapFree) "free" else "non-free"
}

@BindingAdapter("userCurrency")
fun userCurrency(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.currency.toString()
}

@BindingAdapter("userCurrentTradesCount")
fun userCurrentTradesCount(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.currentTradesCount.toString()
}

@BindingAdapter("userTotalTradesCount")
fun userTotalTradesCount(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.totalTradesCount.toString()
}

@BindingAdapter("userTotalTradesVolume")
fun userTotalTradesVolume(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.totalTradesVolume.toString()
}

@BindingAdapter("userCurrentTradesVolume")
fun userCurrentTradesVolume(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.currentTradesVolume.toString()
}

@BindingAdapter("userLeverage")
fun userLeverage(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.leverage.toString()
}

@BindingAdapter("userName")
fun userName(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.name
}

@BindingAdapter("userPhone")
fun userPhone(textView: TextView, phone: String) {
    textView.text = "Phone: $phone"
}

@BindingAdapter("userType")
fun userType(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.type.toString()
}

@BindingAdapter("userVerificationLevel")
fun userVerificationLevel(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.verificationLevel.toString()
}

@BindingAdapter("userZipCode")
fun userZipCode(textView: TextView, accountInformation: AccountInformation) {
    textView.text = accountInformation.zipCode
}