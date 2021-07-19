package com.veton.countrypicker

import com.veton.countrypicker.databinding.CountryPickerBinding

interface CountryPickerListener {
    fun initPickerView(binding: CountryPickerBinding)
    fun setSearch()
    fun setupRecyclerView()
    fun sort()
}