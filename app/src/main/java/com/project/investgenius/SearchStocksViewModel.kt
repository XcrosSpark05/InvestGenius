package com.project.investgenius

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.investgenius.API.Gainer

class SearchStocksViewModel : ViewModel() {
    private val _fullStocksList = MutableLiveData<List<Gainer>>()
    val fullStocksList: LiveData<List<Gainer>> get() = _fullStocksList

    fun setStocks(stocks: List<Gainer>) {
        _fullStocksList.value = stocks
    }
}