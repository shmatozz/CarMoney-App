package com.example.puskiss

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puskiss.databinding.DepositItemBinding

class DepositAdapter: RecyclerView.Adapter<DepositAdapter.DepositHolder>() {

    private val depositList = ArrayList<Deposit>()
    class DepositHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = DepositItemBinding.bind(item)
        fun bind(deposit: Deposit) = with(binding) {
            depositTitle.text = "Вклад"
            depositBalace.text = deposit.currentSum.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepositHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.deposit_item, parent, false)

        return DepositHolder(view)
    }

    override fun getItemCount(): Int {
        return depositList.size
    }

    override fun onBindViewHolder(holder: DepositHolder, position: Int) {
        holder.bind(depositList[position])
    }

    fun addDeposit(deposit: Deposit) {
        depositList.add(deposit)
        notifyDataSetChanged()
    }
}