package com.veton.countrypicker

import com.veton.countrypicker.utils.toEmojiFlag

data class Country(
    val code: String,
    val name: String,
    val dialCodes: List<String>,
    val currency: String
) {
    val flag = code.toEmojiFlag()
    val dialCode: String get() = dialCodes[0]

    init {
        if (dialCodes.isEmpty()) throw IllegalArgumentException("Must provide at least one telephone country code")
    }
}