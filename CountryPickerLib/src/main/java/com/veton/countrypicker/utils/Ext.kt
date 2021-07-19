package com.veton.countrypicker.utils

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast

fun String.toEmojiFlag(): String {
    val flagOffset = 0x1F1E6
    val asciiOffset = 0x41

    val firstChar = Character.codePointAt(this, 0) - asciiOffset + flagOffset
    val secondChar = Character.codePointAt(this, 1) - asciiOffset + flagOffset

    return (String(Character.toChars(firstChar))
            + String(Character.toChars(secondChar)))
}

inline fun TextView.setEnterKeyHandler(crossinline handler: (String) -> Unit) {
    this.setOnEditorActionListener { _, actionId, event ->
        when {
            actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || event.isEnterKey() -> {
                handler(this.text.toString()); true
            }
            else -> false
        }
    }
}

fun KeyEvent?.isEnterKey() = this?.keyCode == KeyEvent.KEYCODE_ENTER || this?.keyCode == KeyEvent.KEYCODE_NUMPAD_ENTER

infix fun String.equalIgnoreCase(text: String) = this.equals(text, ignoreCase = true)

fun Context.toastMsg(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()