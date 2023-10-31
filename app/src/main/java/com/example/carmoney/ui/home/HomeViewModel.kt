package com.example.carmoney.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Calendar

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        val calendar = Calendar.getInstance()
        value = "${calendar.get(Calendar.DAY_OF_MONTH)} ${monthToText(calendar.get(Calendar.MONTH))} "
    }

    private fun monthToText(month: Int) : String {
        when (month) {
            0 -> return "January"
            1 -> return "February"
            2 -> return "March"
            3 -> return "April"
            4 -> return "May"
            5 -> return "June"
            6 -> return "July"
            7 -> return "August"
            8 -> return "September"
            9 -> return "October"
            10 -> return "November"
            11 -> return "December"
        }

        return "null"
    }

    val text: LiveData<String> = _text
}