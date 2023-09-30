package com.example.puskiss.ui.dashboard

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puskiss.Deposit
import com.example.puskiss.DepositAdapter
import com.example.puskiss.R
import com.example.puskiss.databinding.FragmentBankBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentBankBinding? = null
    private val adapter = DepositAdapter()
    private lateinit var prefs: SharedPreferences

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentBankBinding.inflate(inflater, container, false)
        val root: View = binding.root

        prefs = requireActivity().getSharedPreferences("stats", Context.MODE_PRIVATE)

        init()

        return root
    }

    private fun init() {
        binding.apply {
            bankRcView.layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            bankRcView.adapter = adapter

            if (!prefs.contains("DEPOSIT_COUNT")) {
                val editor = prefs.edit()
                editor.putInt("DEPOSIT_COUNT",  0)
                editor.apply()
            } else if (prefs.getInt("DEPOSIT_COUNT", 0) > 0) {
                Log.d("Working", prefs.getInt("DEPOSIT_COUNT", 0).toString())
                for (i in 0 until prefs.getInt("DEPOSIT_COUNT", 0)) {
                    Log.d("Working", prefs.getString("DEPOSIT_TITLE%s".format(i), "nothing")!!)
                    adapter.depositList.add(Deposit(
                        prefs.getString("DEPOSIT_TITLE%s".format(i), "nothing")!!,
                        prefs.getFloat("DEPOSIT_SUM%s".format(i), 0F)
                    ))
                }
            }

            buttonAddDeposit.setOnClickListener {
                adapter.addDeposit(Deposit(getString(R.string.deposit),0F))

                val editor = prefs.edit()
                editor.putInt("DEPOSIT_COUNT", adapter.depositList.size)
                editor.putString("DEPOSIT_TITLE%s".format(adapter.depositList.size - 1), getString(R.string.deposit))
                editor.putFloat("DEPOSIT_SUM%s".format(adapter.depositList.size - 1), 0F)
                editor.apply()
            }

            buttonAddDeposit.setOnLongClickListener {
                adapter.deleteDeposit()

                val editor = prefs.edit()
                editor.putInt("DEPOSIT_COUNT", adapter.depositList.size)
                editor.remove("DEPOSIT_TITLE%s".format(adapter.depositList.size))
                editor.remove("DEPOSIT_SUM%s".format(adapter.depositList.size))
                editor.apply()

                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}