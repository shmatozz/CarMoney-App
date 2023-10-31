package com.example.carmoney

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.carmoney.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.LocalDateTime
import java.util.Calendar
import kotlin.math.pow
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // check if last visit was not today and we need to update our counters
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
        // get preferences
        preferences = getSharedPreferences("stats", Context.MODE_PRIVATE)
        val lastVisitDay = preferences.getInt("LAST_VISIT_DAY", 0)
        val lastVisitMonth = preferences.getInt("LAST_VISIT_MONTH", 0)

        // get current date
        val calendar = Calendar.getInstance()

        // compare current date and last visit date
        if ((calendar.get(Calendar.DAY_OF_MONTH) - lastVisitDay) + (calendar.get(Calendar.MONTH) - lastVisitMonth) != 0) {
            // if day changed -> calculate how many days passed from last visit and update counters
            val daysPass: Int = if (calendar.get(Calendar.DAY_OF_MONTH) > lastVisitDay) {
                calendar.get(Calendar.DAY_OF_MONTH) - lastVisitDay
            } else {
                calendar.get(Calendar.DAY_OF_MONTH) + (length(calendar.get(Calendar.MONTH), isLeap(calendar.get(Calendar.YEAR))) - lastVisitDay)
            }

            // update counters
            addPercentageToDeposit(daysPass)
            moveToDeposit()

            // update last visit info
            val editor = preferences.edit()
            editor.putInt("LAST_VISIT_DAY",  calendar.get(Calendar.DAY_OF_MONTH))
            editor.putInt("LAST_VISIT_MONTH",  calendar.get(Calendar.MONTH))
            editor.apply()
        }
    }

    private fun length(month: Int, leap: Boolean = false): Int {
        if (leap && month == 1) return 29
        when (month) {
            0 -> return 31
            1 -> return 28
            2 -> return 31
            3 -> return 30
            4 -> return 31
            5 -> return 30
            6 -> return 31
            7 -> return 31
            8 -> return 30
            9 -> return 31
            10 -> return 30
            11 -> return 31
        }

        return -1
    }

    private fun isLeap(year: Int) : Boolean {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
    }

    /**
     * Function to add percentages to all deposits
     *
     * @param dayPass - number of days to add percentage for every day
     */
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

    /**
     * Function to move remained balance to deposit and update counters to 0
     */
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