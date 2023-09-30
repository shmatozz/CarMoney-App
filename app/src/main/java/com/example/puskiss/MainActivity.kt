package com.example.puskiss

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.puskiss.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkLastVisit()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun checkLastVisit() {
        preferences = getSharedPreferences("stats", Context.MODE_PRIVATE)
        val lastVisitDay = preferences.getInt("LAST_VISIT_DAY", 0)
        val lastVisitMonth = preferences.getInt("LAST_VISIT_MONTH", 0)

        val calendar = Calendar.getInstance()
        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )

        if ((current.dayOfMonth - lastVisitDay) + (current.month.value - lastVisitMonth) != 0) {
            val daysPass: Int
            if (current.dayOfMonth > lastVisitDay) {
                daysPass = current.dayOfMonth - lastVisitDay
            } else {
                daysPass = current.dayOfMonth + (current.month.length(false) - lastVisitDay)
            }

            addPercentageToDeposit(daysPass)
            moveToDeposit()

            val editor = preferences.edit()
            editor.putInt("LAST_VISIT_DAY",  current.dayOfMonth)
            editor.putInt("LAST_VISIT_MONTH",  current.month.value)
            editor.apply()
        }
    }

    private fun addPercentageToDeposit(dayPass: Int) {
        val countDeposits = preferences.getInt("DEPOSIT_COUNT", 0)

        for (i in 0 until countDeposits) {
            val curPercentage = (1.05).pow(dayPass).toFloat()
            var curSum = preferences.getFloat("DEPOSIT_SUM%s".format(i), 0F)
            curSum = (curSum * curPercentage * 100).roundToInt().toFloat() / 100

            val editor = preferences.edit()
            editor.putFloat("DEPOSIT_SUM%s".format(i),  curSum)
            editor.apply()
        }
    }

    private fun moveToDeposit() {
        val lastTotalCount = preferences.getInt("TOTAL", 0)

        if (preferences.getInt("DEPOSIT_COUNT", 0) != 0) {
            val currentSumDeposit = preferences.getFloat("DEPOSIT_SUM0", 0F)

            val editor = preferences.edit()
            editor.putFloat("DEPOSIT_SUM0", currentSumDeposit + lastTotalCount)
            editor.putInt("TOTAL", 0)
            editor.putInt("YELLOW", 0)
            editor.putInt("GREEN", 0)
            editor.putInt("ORANGE", 0)
            editor.apply()
        }
    }

}