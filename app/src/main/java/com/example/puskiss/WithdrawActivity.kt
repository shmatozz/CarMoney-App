package com.example.puskiss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.puskiss.databinding.ActivityWithdrawBinding

class WithdrawActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWithdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val preferences = getSharedPreferences("stats", MODE_PRIVATE)

        val availableCount = binding.availableCount
        val withdrawCount = binding.withdrawCount
        val minButton = binding.minButton
        val maxButton = binding.maxButton
        val withdrawButton = binding.withdrawBtn
        val warningMsg = binding.warningMsg

        val maxAvailable = preferences.getInt("TOTAL", 0)

        availableCount.text = getString(R.string.available_count, maxAvailable)

        minButton.setOnClickListener {
            withdrawCount.setText("0")
        }

        maxButton.setOnClickListener {
            withdrawCount.setText(maxAvailable.toString())
        }

        withdrawButton.setOnClickListener {
            val enteredCount = if (withdrawCount.text.toString().trim().isNotEmpty()) {
                withdrawCount.text.toString().toInt()
            } else {
                0
            }

            if (enteredCount > maxAvailable) {
                warningMsg.text = getString(R.string.not_enough_warning)
            } else if (enteredCount < 0) {
                warningMsg.text = getString(R.string.negative_warning)
            } else {
                var count = enteredCount
                var yellow = preferences.getInt("YELLOW", 0)
                var green = preferences.getInt("GREEN", 0)
                var orange = preferences.getInt("ORANGE", 0)

                if (yellow - count >= 0) {
                    yellow -= count
                    count = 0
                } else {
                    count -= yellow
                    yellow = 0
                }

                if (green - count >= 0) {
                    green -= count
                    count = 0
                } else {
                    count -= green
                    green = 0
                }

                if (orange - count * 3 >= 0) orange -= (count * 3)

                val editor = preferences.edit()
                editor.putInt("TOTAL", maxAvailable - enteredCount)
                editor.putInt("YELLOW", yellow)
                editor.putInt("GREEN", green)
                editor.putInt("ORANGE", orange)
                editor.apply()

                finish()
            }
        }
    }
}