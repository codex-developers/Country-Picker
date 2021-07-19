package com.bridge.countrypicker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bridge.countrypicker.databinding.ActivityMainBinding
import com.veton.countrypicker.Country
import com.veton.countrypicker.CountryPicker
import com.veton.countrypicker.utils.setEnterKeyHandler

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val picker: CountryPicker = CountryPicker(
        context = this,
        onCountryClicked = ::countrySelected
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnShowCountryPicker.setOnClickListener {
            picker.showCountryPicker(this)
        }

//        binding.edtCode.setEnterKeyHandler {
//            val country = picker.getCountryByDialCode(it.toString())
//            binding.txtCountryByDialCode.text = country?.name
//        }

        binding.edtDialCode.doAfterTextChanged {
            val country = picker.getCountryByDialCode(it.toString())
            binding.txtCountryByDialCode.text = country?.name
        }

        binding.edtFullNumber.doAfterTextChanged {
            val country = picker.getCountryByDialCodeForFullNumber(it.toString())
            binding.txtFullNumber.text = country?.name
        }

        binding.edtCode.doAfterTextChanged {
            val country = picker.getCountryByCode(it.toString())
            binding.txtISOCode.text = country?.name
        }
    }


    private fun countrySelected(country: Country) {
        Toast.makeText(this, "${country.name}", Toast.LENGTH_SHORT).show()
    }
}

