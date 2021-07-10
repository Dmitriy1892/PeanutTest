package com.coldfier.peanuttest

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.updateList() {
    val buffer = this.value
    this.value = buffer
}