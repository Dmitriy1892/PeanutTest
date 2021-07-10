package com.coldfier.peanuttest.quotesfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class QuotesViewModelFactory(private val json: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuotesViewModel::class.java)) {
            return QuotesViewModel(json) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}