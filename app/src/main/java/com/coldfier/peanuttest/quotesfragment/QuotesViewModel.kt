package com.coldfier.peanuttest.quotesfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coldfier.peanuttest.repository.QuoteInfoItem
import com.coldfier.peanuttest.repository.UserData
import com.coldfier.peanuttest.repository.retrofit.ApiClient
import com.google.android.material.chip.Chip
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuotesViewModel(json: String) : ViewModel() {

    private var _quotesList = MutableLiveData<List<QuoteInfoItem>>()
    val quotesList: LiveData<List<QuoteInfoItem>>
        get() = _quotesList

    var startDateTime = MutableLiveData(System.currentTimeMillis())
    var endDateTime = MutableLiveData(System.currentTimeMillis())

    private var checkedChips = mutableListOf<String>()
    private var _pairs = MutableLiveData("")
    val pairs: LiveData<String>
        get() = _pairs

    private var _userData: UserData

    init {
        _userData = initUser(json)
    }

    private fun initUser(json: String): UserData {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(UserData::class.java)
        val userData = jsonAdapter.fromJson(json)

        return userData!!
    }

    fun getQuoteList(
        login: Int = _userData.login,
        pairs: String = _pairs.value ?: "",
        from: Int = startDateTime.value?.div(1000)!!.toInt(),
        to: Int = endDateTime.value?.div(1000)!!.toInt(),
        token: String = _userData.partnerToken
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiService = ApiClient.getPartnerApiService()
            apiService
                .getQuotes(login = login, pairs = pairs, from = from, to = to, token = token)
                .enqueue(object : Callback<List<QuoteInfoItem>> {
                    override fun onResponse(
                        call: Call<List<QuoteInfoItem>>,
                        response: Response<List<QuoteInfoItem>>
                    ) {
                        if (response.isSuccessful) {
                            _quotesList.postValue(response.body())
                        } else {
                            //TODO("Update token")
                        }
                    }

                    override fun onFailure(call: Call<List<QuoteInfoItem>>, t: Throwable) {
                        //TODO("Toast about failed connection")
                    }
                })
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

    fun updatePairs() {
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