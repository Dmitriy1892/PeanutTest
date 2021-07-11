package com.coldfier.peanuttest.signinfragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coldfier.peanuttest.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {

    private var _isFailedSignIn = MutableLiveData<String>()
    val isFailedSignIn: LiveData<String>
        get() = _isFailedSignIn

    private var _authorizationState = MutableLiveData<Boolean>()
    val authorizationState: LiveData<Boolean>
        get() = _authorizationState

    fun signIn(login: Int, password: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val repository = AppRepository.getInstance(context)
                try {
                    if (repository.authorize(login, password)) {
                        _authorizationState.postValue(true)
                    }
                } catch (e: Exception) {
                    _isFailedSignIn.postValue("Check the login/password or internet connection")
                }
        }
    }
}