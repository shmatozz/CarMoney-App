package com.example.puskiss


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.puskiss.databinding.WithdrawDialigBinding

class WithdrawActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = WithdrawDialigBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}