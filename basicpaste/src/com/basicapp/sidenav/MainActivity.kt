package com.basicapp.default

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.basicapp.util.*

class MainActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redirectSystemIOToLog()

    }
}