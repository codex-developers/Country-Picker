package com.veton.countrypicker

import android.content.Context
import android.telephony.TelephonyManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.LinearLayoutManager
import com.veton.countrypicker.databinding.CountryPickerBinding
import com.veton.countrypicker.utils.equalIgnoreCase
import com.veton.countrypicker.utils.toastMsg

class CountryPicker(
    private val context: Context,
    private var onCountryClicked: (Country) -> Unit,
    private var sortBy: Sort? = Sort.NAME,
) : LifecycleObserver, CountryPickerListener, OnCountrySelectedListener {
    private lateinit var countryPickerBinding: CountryPickerBinding
    private val countriesAdapter: CountriesAdapter by lazy {
        CountriesAdapter(this)
    }

    private lateinit var countryPickersView: CountryPickersView

    private var searchResults: ArrayList<Country>? = null

    private val countries = arrayListOf(
        Country("AD", "Andorra", listOf("+376"), "EUR"),
        Country("AF", "Afghanistan", listOf("+93"), "AFN"),
        Country("AG", "Antigua and Barbuda", listOf("+1-268"), "XCD"),
        Country("AI", "Anguilla", listOf("+1-264"), "XCD"),
        Country("AL", "Albania", listOf("+355"), "ALL"),
        Country("AM", "Armenia", listOf("+374"), "AMD"),
        Country("AN", "Netherlands Antilles", listOf("+599"), "ANG"),
        Country("AO", "Angola", listOf("+244"), "AOA"),
        Country("AQ", "Antarctica", listOf("+672"), "USD"),
        Country("AR", "Argentina", listOf("+54"), "ARS"),
        Country("AS", "American Samoa", listOf("+1-684"), "USD"),
        Country("AT", "Austria", listOf("+43"), "EUR"),
        Country("AU", "Australia", listOf("+61"), "AUD"),
        Country("AW", "Aruba", listOf("+297"), "AWG"),
        Country("AZ", "Azerbaijan", listOf("+994"), "AZN"),
        Country("BA", "Bosnia and Herzegovina", listOf("+387"), "BAM"),
        Country("BB", "Barbados", listOf("+1-246"), "BBD"),
        Country("BD", "Bangladesh", listOf("+880"), "BDT"),
        Country("BE", "Belgium", listOf("+32"), "EUR"),
        Country("BF", "Burkina Faso", listOf("+226"), "XOF"),
        Country("BG", "Bulgaria", listOf("+359"), "BGN"),
        Country("BH", "Bahrain", listOf("+973"), "BHD"),
        Country("BI", "Burundi", listOf("+257"), "BIF"),
        Country("BJ", "Benin", listOf("+229"), "XOF"),
        Country("BL", "Saint Barthelemy", listOf("+590"), "EUR"),
        Country("BM", "Bermuda", listOf("+1-441"), "BMD"),
        Country("BN", "Brunei", listOf("+673"), "BND"),
        Country("BO", "Bolivia", listOf("+591"), "BOB"),
        Country("BR", "Brazil", listOf("+55"), "BRL"),
        Country("BS", "Bahamas", listOf("+1-242"), "BSD"),
        Country("BT", "Bhutan", listOf("+975"), "BTN"),
        Country("BW", "Botswana", listOf("+267"), "BWP"),
        Country("BY", "Belarus", listOf("+375"), "BYR"),
        Country("BZ", "Belize", listOf("+501"), "BZD"),
        Country(
            "CA",
            "Canada",
            listOf(
                "+1-204",
                "+1-226",
                "+1-236",
                "+1-249",
                "+1-250",
                "+1-289",
                "+1-306",
                "+1-343",
                "+1-365",
                "+1-367",
                "+1-403",
                "+1-416",
                "+1-418",
                "+1-431",
                "+1-437",
                "+1-438",
                "+1-450",
                "+1-506",
                "+1-514",
                "+1-519",
                "+1-548",
                "+1-579",
                "+1-581",
                "+1-587",
                "+1-604",
                "+1-613",
                "+1-639",
                "+1-647",
                "+1-672",
                "+1-705",
                "+1-709",
                "+1-778",
                "+1-780",
                "+1-782",
                "+1-807",
                "+1-819",
                "+1-825",
                "+1-867",
                "+1-873",
                "+1-902",
                "+1-905"
            ),
            "CAD"
        ),
        Country("CC", "Cocos Islands", listOf("+61"), "AUD"),
        Country("CD", "Democratic Republic of the Congo", listOf("+243"), "CDF"),
        Country("CF", "Central African Republic", listOf("+236"), "XAF"),
        Country("CG", "Republic of the Congo", listOf("+242"), "XAF"),
        Country("CH", "Switzerland", listOf("+41"), "CHF"),
        Country("CI", "Ivory Coast", listOf("+225"), "XOF"),
        Country("CK", "Cook Islands", listOf("+682"), "NZD"),
        Country("CL", "Chile", listOf("+56"), "CLP"),
        Country("CM", "Cameroon", listOf("+237"), "XAF"),
        Country("CN", "China", listOf("+86"), "CNY"),
        Country("CO", "Colombia", listOf("+57"), "COP"),
        Country("CR", "Costa Rica", listOf("+506"), "CRC"),
        Country("CU", "Cuba", listOf("+53"), "CUP"),
        Country("CV", "Cape Verde", listOf("+238"), "CVE"),
        Country("CW", "Curacao", listOf("+599"), "ANG"),
        Country("CX", "Christmas Island", listOf("+61"), "AUD"),
        Country("CY", "Cyprus", listOf("+357"), "EUR"),
        Country("CZ", "Czech Republic", listOf("+420"), "CZK"),
        Country("DE", "Germany", listOf("+49"), "EUR"),
        Country("DJ", "Djibouti", listOf("+253"), "DJF"),
        Country("DK", "Denmark", listOf("+45"), "DKK"),
        Country("DM", "Dominica", listOf("+1-767"), "XCD"),
        Country("DO", "Dominican Republic", listOf("+1-809", "+1-829", "+1-849"), "DOP"),
        Country("DZ", "Algeria", listOf("+213"), "DZD"),
        Country("EC", "Ecuador", listOf("+593"), "USD"),
        Country("EE", "Estonia", listOf("+372"), "EUR"),
        Country("EG", "Egypt", listOf("+20"), "EGP"),
        Country("EH", "Western Sahara", listOf("+212"), "MAD"),
        Country("ER", "Eritrea", listOf("+291"), "ERN"),
        Country("ES", "Spain", listOf("+34"), "EUR"),
        Country("ET", "Ethiopia", listOf("+251"), "ETB"),
        Country("FI", "Finland", listOf("+358"), "EUR"),
        Country("FJ", "Fiji", listOf("+679"), "FJD"),
        Country("FK", "Falkland Islands", listOf("+500"), "FKP"),
        Country("FM", "Micronesia", listOf("+691"), "USD"),
        Country("FO", "Faroe Islands", listOf("+298"), "DKK"),
        Country("FR", "France", listOf("+33"), "EUR"),
        Country("GA", "Gabon", listOf("+241"), "XAF"),
        Country("GB", "United Kingdom", listOf("+44"), "GBP"),
        Country("GD", "Grenada", listOf("+1-473"), "XCD"),
        Country("GE", "Georgia", listOf("+995"), "GEL"),
        Country("GG", "Guernsey", listOf("+44-1481"), "GGP"),
        Country("GH", "Ghana", listOf("+233"), "GHS"),
        Country("GI", "Gibraltar", listOf("+350"), "GIP"),
        Country("GL", "Greenland", listOf("+299"), "DKK"),
        Country("GM", "Gambia", listOf("+220"), "GMD"),
        Country("GN", "Guinea", listOf("+224"), "GNF"),
        Country("GQ", "Equatorial Guinea", listOf("+240"), "XAF"),
        Country("GR", "Greece", listOf("+30"), "EUR"),
        Country("GT", "Guatemala", listOf("+502"), "GTQ"),
        Country("GU", "Guam", listOf("+1-671"), "USD"),
        Country("GW", "Guinea-Bissau", listOf("+245"), "XOF"),
        Country("GY", "Guyana", listOf("+592"), "GYD"),
        Country("HK", "Hong Kong", listOf("+852"), "HKD"),
        Country("HN", "Honduras", listOf("+504"), "HNL"),
        Country("HR", "Croatia", listOf("+385"), "HRK"),
        Country("HT", "Haiti", listOf("+509"), "HTG"),
        Country("HU", "Hungary", listOf("+36"), "HUF"),
        Country("ID", "Indonesia", listOf("+62"), "IDR"),
        Country("IE", "Ireland", listOf("+353"), "EUR"),
        Country("IL", "Israel", listOf("+972"), "ILS"),
        Country("IM", "Isle of Man", listOf("+44-1624"), "GBP"),
        Country("IN", "India", listOf("+91"), "INR"),
        Country("IO", "British Indian Ocean Territory", listOf("+246"), "USD"),
        Country("IQ", "Iraq", listOf("+964"), "IQD"),
        Country("IR", "Iran", listOf("+98"), "IRR"),
        Country("IS", "Iceland", listOf("+354"), "ISK"),
        Country("IT", "Italy", listOf("+39"), "EUR"),
        Country("JE", "Jersey", listOf("+44-1534"), "JEP"),
        Country("JM", "Jamaica", listOf("+1-876"), "JMD"),
        Country("JO", "Jordan", listOf("+962"), "JOD"),
        Country("JP", "Japan", listOf("+81"), "JPY"),
        Country("KE", "Kenya", listOf("+254"), "KES"),
        Country("KG", "Kyrgyzstan", listOf("+996"), "KGS"),
        Country("KH", "Cambodia", listOf("+855"), "KHR"),
        Country("KI", "Kiribati", listOf("+686"), "AUD"),
        Country("KM", "Comoros", listOf("+269"), "KMF"),
        Country("KN", "Saint Kitts and Nevis", listOf("+1-869"), "XCD"),
        Country("KP", "North Korea", listOf("+850"), "KPW"),
        Country("KR", "South Korea", listOf("+82"), "KRW"),
        Country("KW", "Kuwait", listOf("+965"), "KWD"),
        Country("KY", "Cayman Islands", listOf("+1-345"), "KYD"),
        Country("KZ", "Kazakhstan", listOf("+7"), "KZT"),
        Country("LA", "Laos", listOf("+856"), "LAK"),
        Country("LB", "Lebanon", listOf("+961"), "LBP"),
        Country("LC", "Saint Lucia", listOf("+1-758"), "XCD"),
        Country("LI", "Liechtenstein", listOf("+423"), "CHF"),
        Country("LK", "Sri Lanka", listOf("+94"), "LKR"),
        Country("LR", "Liberia", listOf("+231"), "LRD"),
        Country("LS", "Lesotho", listOf("+266"), "LSL"),
        Country("LT", "Lithuania", listOf("+370"), "LTL"),
        Country("LU", "Luxembourg", listOf("+352"), "EUR"),
        Country("LV", "Latvia", listOf("+371"), "LVL"),
        Country("LY", "Libya", listOf("+218"), "LYD"),
        Country("MA", "Morocco", listOf("+212"), "MAD"),
        Country("MC", "Monaco", listOf("+377"), "EUR"),
        Country("MD", "Moldova", listOf("+373"), "MDL"),
        Country("ME", "Montenegro", listOf("+382"), "EUR"),
        Country("MF", "Saint Martin", listOf("+590"), "EUR"),
        Country("MG", "Madagascar", listOf("+261"), "MGA"),
        Country("MH", "Marshall Islands", listOf("+692"), "USD"),
        Country("MK", "North Macedonia", listOf("+389"), "MKD"),
        Country("ML", "Mali", listOf("+223"), "XOF"),
        Country("MM", "Myanmar", listOf("+95"), "MMK"),
        Country("MN", "Mongolia", listOf("+976"), "MNT"),
        Country("MO", "Macao", listOf("+853"), "MOP"),
        Country("MP", "Northern Mariana Islands", listOf("+1-670"), "USD"),
        Country("MR", "Mauritania", listOf("+222"), "MRO"),
        Country("MS", "Montserrat", listOf("+1-664"), "XCD"),
        Country("MT", "Malta", listOf("+356"), "EUR"),
        Country("MU", "Mauritius", listOf("+230"), "MUR"),
        Country("MV", "Maldives", listOf("+960"), "MVR"),
        Country("MW", "Malawi", listOf("+265"), "MWK"),
        Country("MX", "Mexico", listOf("+52"), "MXN"),
        Country("MY", "Malaysia", listOf("+60"), "MYR"),
        Country("MZ", "Mozambique", listOf("+258"), "MZN"),
        Country("NA", "Namibia", listOf("+264"), "NAD"),
        Country("NC", "New Caledonia", listOf("+687"), "XPF"),
        Country("NE", "Niger", listOf("+227"), "XOF"),
        Country("NG", "Nigeria", listOf("+234"), "NGN"),
        Country("NI", "Nicaragua", listOf("+505"), "NIO"),
        Country("NL", "Netherlands", listOf("+31"), "EUR"),
        Country("NO", "Norway", listOf("+47"), "NOK"),
        Country("NP", "Nepal", listOf("+977"), "NPR"),
        Country("NR", "Nauru", listOf("+674"), "AUD"),
        Country("NU", "Niue", listOf("+683"), "NZD"),
        Country("NZ", "New Zealand", listOf("+64"), "NZD"),
        Country("OM", "Oman", listOf("+968"), "OMR"),
        Country("PA", "Panama", listOf("+507"), "PAB"),
        Country("PE", "Peru", listOf("+51"), "PEN"),
        Country("PF", "French Polynesia", listOf("+689"), "XPF"),
        Country("PG", "Papua New Guinea", listOf("+675"), "PGK"),
        Country("PH", "Philippines", listOf("+63"), "PHP"),
        Country("PK", "Pakistan", listOf("+92"), "PKR"),
        Country("PL", "Poland", listOf("+48"), "PLN"),
        Country("PM", "Saint Pierre and Miquelon", listOf("+508"), "EUR"),
        Country("PN", "Pitcairn", listOf("+64"), "NZD"),
        Country("PR", "Puerto Rico", listOf("+1-787", "+1-939"), "USD"),
        Country("PS", "Palestinian", listOf("+970"), "ILS"),
        Country("PT", "Portugal", listOf("+351"), "EUR"),
        Country("PW", "Palau", listOf("+680"), "USD"),
        Country("PY", "Paraguay", listOf("+595"), "PYG"),
        Country("QA", "Qatar", listOf("+974"), "QAR"),
        Country("RE", "Reunion", listOf("+262"), "EUR"),
        Country("RO", "Romania", listOf("+40"), "RON"),
        Country("RS", "Serbia", listOf("+381"), "RSD"),
        Country("RU", "Russia", listOf("+7"), "RUB"),
        Country("RW", "Rwanda", listOf("+250"), "RWF"),
        Country("SA", "Saudi Arabia", listOf("+966"), "SAR"),
        Country("SB", "Solomon Islands", listOf("+677"), "SBD"),
        Country("SC", "Seychelles", listOf("+248"), "SCR"),
        Country("SD", "Sudan", listOf("+249"), "SDG"),
        Country("SE", "Sweden", listOf("+46"), "SEK"),
        Country("SG", "Singapore", listOf("+65"), "SGD"),
        Country("SH", "Saint Helena", listOf("+290"), "SHP"),
        Country("SI", "Slovenia", listOf("+386"), "EUR"),
        Country("SJ", "Svalbard and Jan Mayen", listOf("+47"), "NOK"),
        Country("SK", "Slovakia", listOf("+421"), "EUR"),
        Country("SL", "Sierra Leone", listOf("+232"), "SLL"),
        Country("SM", "San Marino", listOf("+378"), "EUR"),
        Country("SN", "Senegal", listOf("+221"), "XOF"),
        Country("SO", "Somalia", listOf("+252"), "SOS"),
        Country("SR", "Suriname", listOf("+597"), "SRD"),
        Country("SS", "South Sudan", listOf("+211"), "SSP"),
        Country("ST", "Sao Tome and Principe", listOf("+239"), "STD"),
        Country("SV", "El Salvador", listOf("+503"), "SVC"),
        Country("SX", "Sint Maarten", listOf("+1-721"), "ANG"),
        Country("SY", "Syria", listOf("+963"), "SYP"),
        Country("SZ", "Swaziland", listOf("+268"), "SZL"),
        Country("TC", "Turks and Caicos Islands", listOf("+1-649"), "USD"),
        Country("TD", "Chad", listOf("+235"), "XAF"),
        Country("TG", "Togo", listOf("+228"), "XOF"),
        Country("TH", "Thailand", listOf("+66"), "THB"),
        Country("TJ", "Tajikistan", listOf("+992"), "TJS"),
        Country("TK", "Tokelau", listOf("+690"), "NZD"),
        Country("TL", "East Timor", listOf("+670"), "USD"),
        Country("TM", "Turkmenistan", listOf("+993"), "TMT"),
        Country("TN", "Tunisia", listOf("+216"), "TND"),
        Country("TO", "Tonga", listOf("+676"), "TOP"),
        Country("TR", "Turkey", listOf("+90"), "TRY"),
        Country("TT", "Trinidad and Tobago", listOf("+1-868"), "TTD"),
        Country("TV", "Tuvalu", listOf("+688"), "AUD"),
        Country("TW", "Taiwan", listOf("+886"), "TWD"),
        Country("TZ", "Tanzania", listOf("+255"), "TZS"),
        Country("UA", "Ukraine", listOf("+380"), "UAH"),
        Country("UG", "Uganda", listOf("+256"), "UGX"),
        Country("AE", "United Arab Emirates", listOf("+971"), "AED"),
        Country("US", "United States", listOf("+1"), "USD"),
        Country("UY", "Uruguay", listOf("+598"), "UYU"),
        Country("UZ", "Uzbekistan", listOf("+998"), "UZS"),
        Country("VA", "Vatican", listOf("+379"), "EUR"),
        Country("VC", "Saint Vincent and the Grenadines", listOf("+1-784"), "XCD"),
        Country("VE", "Venezuela", listOf("+58"), "VEF"),
        Country("VG", "British Virgin Islands", listOf("+1-284"), "USD"),
        Country("VI", "U.S. Virgin Islands", listOf("+1-340"), "USD"),
        Country("VN", "Vietnam", listOf("+84"), "VND"),
        Country("VU", "Vanuatu", listOf("+678"), "VUV"),
        Country("WF", "Wallis and Futuna", listOf("+681"), "XPF"),
        Country("WS", "Samoa", listOf("+685"), "WST"),
        Country("XK", "Kosovo", listOf("+383"), "EUR"),
        Country("YE", "Yemen", listOf("+967"), "YER"),
        Country("YT", "Mayotte", listOf("+262"), "EUR"),
        Country("ZA", "South Africa", listOf("+27"), "ZAR"),
        Country("ZM", "Zambia", listOf("+260"), "ZMW"),
        Country("ZW", "Zimbabwe", listOf("+263"), "USD")
    )

    init {
        sortCountries(countries)
    }

    fun sortCountries(countries: ArrayList<Country>) {
        when (sortBy) {
            Sort.NAME -> countries.sortBy { it.name }
            Sort.ISO -> countries.sortBy { it.code }
            Sort.DIAL_CODE -> countries.sortBy { it.dialCode }
        }
    }

    fun showCountryPicker(activity: AppCompatActivity) {
        activity.lifecycle.addObserver(this)
        countryPickersView = CountryPickersView.newInstance(countryPickerListener = this, onDismissDialog = {
            searchResults?.clear()
            searchResults?.addAll(countries)
            countryPickersView.dismissAllowingStateLoss()
        }).apply {
            show(activity.supportFragmentManager, "Country Picker View")
        }
    }

    override fun initPickerView(binding: CountryPickerBinding) {
        countryPickerBinding = binding
    }

    override fun setSearch() {
        countryPickerBinding.search.doAfterTextChanged {
            search(it.toString())
        }
    }

    override fun sort() {
        countryPickerBinding.imgSort.setOnClickListener {
            when (sortBy) {
                Sort.NAME -> sortBy = Sort.DIAL_CODE
                Sort.DIAL_CODE -> sortBy = Sort.ISO
                Sort.ISO -> sortBy = Sort.NAME
            }
            sortCountries(searchResults ?: countries)
            countriesAdapter.notifyDataSetChanged()

            context.toastMsg(context.getString(R.string.sorted_by, sortBy?.sortName))
        }
    }

    private fun search(query: String) {
        searchResults?.clear()

        countries.forEach {
            if (it.name.lowercase().contains(query.lowercase()))
                searchResults?.add(it)
        }
        countriesAdapter.notifyDataSetChanged()
    }

    override fun setupRecyclerView() {
        searchResults = ArrayList<Country>().apply {
            ensureCapacity(countries.size)
            addAll(countries)
        }

        countryPickerBinding.rvCountries.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        countriesAdapter.submitList(searchResults)
    }

    override fun onCountrySelected(country: Country) {
        onCountryClicked(country)
        countryPickersView.dismissAllowingStateLoss()
    }


    fun getCountryByCode(code: String) = countries.find {
        it.code equalIgnoreCase code
    }

    fun getCountryByName(name: String) = countries.find {
        it.name equalIgnoreCase name
    }

    fun getCountryByDialCode(dialCode: String) = countries.find {
        it.dialCodes.contains(dialCode)
    }

    val countryFromSIM: Country?
        get() {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return if (telephonyManager.simState != TelephonyManager.SIM_STATE_ABSENT) {
                getCountryByCode(telephonyManager.simCountryIso)
            } else getCountryByCode("MK")
        }

    fun getCountryByDialCodeForFullNumber(fullPhoneNumber: String): Country? {
        val phoneNumberDigitsOnly = fullPhoneNumber.filter { it.isDigit() }
        var bestMatch: Country? = null
        var bestMatchLength = 0
        countries.forEach { country ->
            country.dialCodes.forEach { prefix ->
                val prefixDigitsOnly = prefix.filter { it.isDigit() }
                if (prefixDigitsOnly.length > bestMatchLength && phoneNumberDigitsOnly.startsWith(
                        prefixDigitsOnly
                    )
                ) {
                    bestMatch = country
                    bestMatchLength = prefix.length
                }
            }
        }
        return bestMatch
    }

    enum class Sort(val sortName: String) {
        NAME("Name"),
        ISO("ISO code"),
        DIAL_CODE("Dial code")
    }
}