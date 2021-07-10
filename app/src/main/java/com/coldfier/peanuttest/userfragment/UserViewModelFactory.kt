package com.coldfier.peanuttest.userfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class UserViewModelFactory(private val json: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(json = json) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}