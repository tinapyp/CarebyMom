package com.dicoding.carebymom.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        val currentDate = SimpleDateFormat("EEEE, \nd MMMM yyyy", Locale.getDefault()).format(Date())
        value = "$currentDate"
    }

    val text: LiveData<String> = _text
}