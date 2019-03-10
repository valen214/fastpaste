package com.basicapp.util


import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.view.View
import android.view.inputmethod.InputMethodManager

/*

fun Activity.hideKeyboard(){
    val imm: InputMethodManager = (getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    listOf(R.id.activity_main_input1, R.id.activity_main_input2).forEach{
        val view: View = findViewById(it)
        view.setOnFocusChangeListener{ v, _ ->
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
        }
    }
    imm.hideSoftInputFromWindow(
            getWindow().getDecorView().getRootView().getWindowToken(), 0)
}
*/


fun KeyboardView.initNumpad(act: Activity, xmlLayoutResId: Int,
        keyboardActionListener: KeyboardView.OnKeyboardActionListener):
        KeyboardView{
    Keyboard(act, xmlLayoutResId).let{ keyboard ->
        this.setKeyboard(keyboard)
    }
    this.setPreviewEnabled(false)
    this.setOnKeyboardActionListener(keyboardActionListener)
    return this
}
