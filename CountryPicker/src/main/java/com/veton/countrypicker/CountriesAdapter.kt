package com.veton.countrypicker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.veton.countrypicker.databinding.CountryListCellBinding

class CountriesAdapter(
    private val onCountrySelectedListener: OnCountrySelectedListener
) : ListAdapter<Country, CountriesAdapter.ViewHolder>(CountriesComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CountryListCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmp = getItem(position)
        if (tmp != null){
            holder.bind(tmp)
            holder.itemView.setOnClickListener {
                onCountrySelectedListener.onCountrySelected(tmp)
            }
        }
    }

    inner class ViewHolder(private val binding: CountryListCellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(country: Country) {
            binding.txtFlagName.text = binding.root.context.getString(R.string.flag_name, country.flag, country.name)
        }
    }

    internal class CountriesComparator : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.code == newItem.code
        }
    }
}