package com.example.carmoney.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.carmoney.R
import com.example.carmoney.WithdrawActivity
import com.example.carmoney.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var yellowButton: Button
    private lateinit var greenButton: Button
    private lateinit var orangeButton: Button
    private lateinit var withdrawButton: Button
    private lateinit var totalCount: TextView

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.date
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        prefs = requireActivity().getSharedPreferences("stats", Context.MODE_PRIVATE)

        yellowButton = binding.yellowPlus
        greenButton = binding.greenPlus
        orangeButton = binding.orangePlus
        withdrawButton = binding.withdrawButton
        totalCount = binding.totalCounter

        totalCount.text = prefs.getInt("TOTAL", 0).toString()

        yellowButton.text = prefs.getInt("YELLOW", 0).toString()
        greenButton.text = prefs.getInt("GREEN", 0).toString()
        orangeButton.text = prefs.getInt("ORANGE", 0).toString()

        yellowButton.setOnClickListener {
            colorButtonPressed(yellowButton)
        }

        yellowButton.setOnLongClickListener {
            longButtonPressed(yellowButton)
        }

        greenButton.setOnClickListener {
            colorButtonPressed(greenButton)
        }

        greenButton.setOnLongClickListener {
            longButtonPressed(greenButton)
        }

        orangeButton.setOnClickListener {
            colorButtonPressed(orangeButton)
        }

        orangeButton.setOnLongClickListener {
            longButtonPressed(orangeButton)
        }

        withdrawButton.setOnClickListener {
            openWithdrawMenu()
        }

        return root
    }

    private fun colorButtonPressed(button: Button) {
        val curCount = button.text
        button.text = getString(R.string.number, curCount.toString().toInt() + 1)

        refreshTotalCount()
    }

    private fun longButtonPressed(button: Button) : Boolean {
        val curCount = button.text
        if (curCount != "0") {
            button.text = getString(R.string.number, curCount.toString().toInt() - 1)
        }

        refreshTotalCount()

        return true
    }

    private fun openWithdrawMenu() {
        val intent = Intent(context, WithdrawActivity::class.java)
        startActivity(intent)
    }

    private fun refreshTotalCount() {
        val curCount =
                    binding.yellowPlus.text.toString().toInt() +
                    binding.greenPlus.text.toString().toInt() +
                    binding.orangePlus.text.toString().toInt() / 3

        binding.totalCounter.text = getString(R.string.number, curCount)
    }

    override fun onPause() {
        super.onPause()

        val editor = prefs.edit()
        editor.putInt("YELLOW",  binding.yellowPlus.text.toString().toInt())
        editor.putInt("GREEN", binding.greenPlus.text.toString().toInt())
        editor.putInt("ORANGE", binding.orangePlus.text.toString().toInt())
        editor.putInt("TOTAL", binding.totalCounter.text.toString().toInt())
        editor.apply()
    }

    override fun onResume() {
        super.onResume()

        binding.totalCounter.text = prefs.getInt("TOTAL", 0).toString()
        binding.yellowPlus.text = prefs.getInt("YELLOW", 0).toString()
        binding.greenPlus.text = prefs.getInt("GREEN", 0).toString()
        binding.orangePlus.text = prefs.getInt("ORANGE", 0).toString()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}