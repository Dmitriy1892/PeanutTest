package com.coldfier.peanuttest.userfragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.coldfier.peanuttest.repository.AccountInformation
import com.coldfier.peanuttest.repository.AppRepository
import com.coldfier.peanuttest.repository.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private var _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData>
        get() = _userData

    private var _accountInformation = MutableLiveData(AccountInformation())
    val accountInformation: LiveData<AccountInformation>
        get() = _accountInformation

    private var _phoneNumber = MutableLiveData("")
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    private var _toastCatcher = MutableLiveData(false)
    val toastCatcher: LiveData<Boolean>
        get() = _toastCatcher

    init {
        initUser(application.applicationContext)
    }

    private fun initUser(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val repo = AppRepository.getInstance(context)
            _userData.postValue(repo.getAccount())
        }
    }

    fun getAccountInformation(userData: UserData, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val repository = AppRepository.getInstance(context)

            //get account info
            try {
                _accountInformation.postValue(repository.getAccountInfo(userData))
            } catch (e: Exception) {
                try {
                    if (repository.updateAccount(userData)) {
                        _userData.postValue(repository.getAccount())
                        _accountInformation.postValue(repository.getAccountInfo(repository.getAccount()!!))
                    } else {
                        _toastCatcher.postValue(true)
                    }
                } catch (e: Exception) {
                    _toastCatcher.postValue(true)
                }
            }

            //get phone number
            try {
                _phoneNumber.postValue(repository.getAccountPhoneNumber(userData))
            } catch (e: Exception) {
                try {
                    if (repository.updateAccount(userData)) {
                        _userData.postValue(repository.getAccount())
                        _phoneNumber.postValue(repository.getAccountPhoneNumber(repository.getAccount()!!))
                    } else {
                        _toastCatcher.postValue(true)
                    }
                } catch (e: Exception) {
                    _toastCatcher.postValue(true)
                }
            }
        }
    }
}