package com.veton.countrypicker

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.veton.countrypicker.databinding.CountryPickerBinding

class CountryPickersView(
    private val countryPickerListener: CountryPickerListener,
    private val onDismissDialog: () -> Unit
) : BottomSheetDialogFragment() {
    private lateinit var binding: CountryPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CountryPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resizeDialog()

        countryPickerListener.apply {
            initPickerView(binding)
            setSearch()
            setupRecyclerView()
            sort()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissDialog()
    }

    private fun resizeDialog() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        dialog?.window?.setDecorFitsSystemWindows(false)
    } else dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    companion object {
        fun newInstance(
            countryPickerListener: CountryPickerListener,
            onDismissDialog: () -> Unit
        ): CountryPickersView {
            return CountryPickersView(countryPickerListener, onDismissDialog)
        }
    }
}