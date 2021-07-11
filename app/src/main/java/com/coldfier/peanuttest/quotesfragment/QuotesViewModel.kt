package com.coldfier.peanuttest.quotesfragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.coldfier.peanuttest.repository.AppRepository
import com.coldfier.peanuttest.repository.QuoteInfoItem
import com.coldfier.peanuttest.repository.UserData
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class QuotesViewModel(private val app: Application) : AndroidViewModel(app) {

    private var _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData>
        get() = _userData

    private var _quotesList = MutableLiveData<List<QuoteInfoItem>>()
    val quotesList: LiveData<List<QuoteInfoItem>>
        get() = _quotesList

    var startDateTime = MutableLiveData(System.currentTimeMillis())
    var endDateTime = MutableLiveData(System.currentTimeMillis())

    private var checkedChips = mutableListOf<String>()

    private var _pairs = MutableLiveData("")
    val pairs: LiveData<String>
        get() = _pairs

    private var _toastCatcher = MutableLiveData(false)
    val toastCatcher: LiveData<Boolean>
        get() = _toastCatcher

    init {
        initUser(app.applicationContext)
    }

    private fun initUser(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val repo = AppRepository.getInstance(context)
            _userData.postValue(repo.getAccount())
        }
    }

    fun getQuoteList(
        userData: UserData = _userData.value ?: UserData(login = 0, password = ""),
        pairs: String = _pairs.value ?: "",
        from: Int = startDateTime.value?.div(1000)!!.toInt(),
        to: Int = endDateTime.value?.div(1000)!!.toInt(),
        context: Context = app.applicationContext
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val repository = AppRepository.getInstance(context)

            try {
                val listResponse = repository
                    .getQuotesList(login = userData.login, pairs = pairs, from = from, to = to, token = userData.partnerToken)
                _quotesList.postValue(listResponse)
            } catch (e: Exception) {
                try {
                    if (repository.updateAccount(userData)) {
                        val listResponse = repository
                            .getQuotesList(login = userData.login, pairs = pairs, from = from, to = to, token = userData.partnerToken)
                        _quotesList.postValue(listResponse)
                        _userData.postValue(repository.getAccount())
                    } else {
                        _toastCatcher.postValue(true)
                    }
                } catch (e: Exception) {
                    _toastCatcher.postValue(true)
                }
            }
        }
    }

    fun addCheckedChip(chip: Chip) {
        viewModelScope.launch {
            checkedChips.add(chip.text.toString())
            updatePairs()
        }
    }

    fun deleteUncheckedChip(chip: Chip) {
        viewModelScope.launch {
            if (checkedChips.contains(chip.text.toString())) {
                checkedChips.remove(chip.text.toString())
                updatePairs()
            }
        }
    }

    fun clearPairsList() {
        checkedChips.clear()
    }

    private fun updatePairs() {
        viewModelScope.launch {
            var pairsBuffer = ""
            checkedChips.forEachIndexed { index, s ->
                if (index == 0) {
                    pairsBuffer = s
                } else {
                    pairsBuffer += ",$s"
                }
            }
            _pairs.value = pairsBuffer
        }
    }
}