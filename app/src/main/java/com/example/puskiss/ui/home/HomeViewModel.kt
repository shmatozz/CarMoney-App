package com.example.puskiss.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime
import java.util.Calendar

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        val calendar = Calendar.getInstance()
        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
        value = "${current.dayOfMonth} ${current.month.plus(1)} "
    }

    val text: LiveData<String> = _text
}