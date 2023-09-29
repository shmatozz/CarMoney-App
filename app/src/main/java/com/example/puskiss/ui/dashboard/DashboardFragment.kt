package com.example.puskiss.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puskiss.Deposit
import com.example.puskiss.DepositAdapter
import com.example.puskiss.databinding.FragmentBankBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentBankBinding? = null
    private val adapter = DepositAdapter()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentBankBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()

        return root
    }

    private fun init() {
        binding.apply {
            bankRcView.layoutManager = LinearLayoutManager(this@DashboardFragment.context)
            bankRcView.adapter = adapter
            buttonAddDeposit.setOnClickListener {
                adapter.addDeposit(Deposit(0F))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}