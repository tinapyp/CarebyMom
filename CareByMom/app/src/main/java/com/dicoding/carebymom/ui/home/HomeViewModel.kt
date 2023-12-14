package com.dicoding.carebymom.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _textDate = MutableLiveData<String>().apply {
        val currentDate = SimpleDateFormat("EEEE, \nd MMMM yyyy", Locale.getDefault()).format(Date())
        value = "$currentDate"
    }

    private val _textDays = MutableLiveData<String>().apply {
        val currentDays = "120"
        value = "$currentDays\nDays"
    }

    private val _textGrowth = MutableLiveData<String>().apply {
        val growth = "Babies can already eat and earn their own money."
        value = "$growth"
    }

    val textDate: LiveData<String> = _textDate
    val textDays : LiveData<String> = _textDays
    val textGrowth : LiveData<String> = _textGrowth

}