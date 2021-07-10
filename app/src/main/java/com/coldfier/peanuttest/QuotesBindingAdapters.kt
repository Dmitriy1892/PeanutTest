package com.coldfier.peanuttest

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.coldfier.peanuttest.repository.QuoteInfoItem
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@BindingAdapter("actualTime")
fun actualTime(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    val date = Date(quoteInfoItem.actualTime.toLong()*1000)
    val sdf = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss").format(date)
    textView.text = sdf
}

@BindingAdapter("comment")
fun comment(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.comment
}

@BindingAdapter("price")
fun price(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.price.toString()
}

@BindingAdapter("sl")
fun sl(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.sl.toString()
}

@BindingAdapter("tp")
fun tp(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.tp.toString()
}

@BindingAdapter("period")
fun period(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.period
}

@BindingAdapter("tradingSystem")
fun tradingSystem(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.tradingSystem.toString()
}

@BindingAdapter("cmd")
fun cmd(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.cmd.toString()
}

@BindingAdapter("pair")
fun pair(textView: TextView, quoteInfoItem: QuoteInfoItem) {
    textView.text = quoteInfoItem.pair
}