package com.randommain


import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

import com.randommain.fastpaste.R

fun Activity.hideKeyboard(){
    val imm: InputMethodManager = (getSystemService(
            Context.INPUT_METHOD_SERVICE) as InputMethodManager)
    listOf(R.id.activity_main_input1, R.id.activity_main_input2).forEach{
        val view: View = findViewById(it)
        view.setOnFocusChangeListener{ v, hasFocus ->
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            println("HI")
        }
    }
    imm.hideSoftInputFromWindow(
            getWindow().getDecorView().getRootView().getWindowToken(), 0)
}