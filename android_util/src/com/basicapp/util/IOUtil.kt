package com.basicapp.util


import android.util.Log

import java.io.OutputStream
import java.io.PrintStream

fun redirectSystemIOToLog(tag: String = "MyActivity"){
    PrintStream(object: OutputStream(){
        private val line_buffer: StringBuilder = StringBuilder()
        override fun write(b: Int){
            when(b){
                10 -> { // LF is 10, CR is 13
                    Log.i(tag, line_buffer.toString())
                    line_buffer.setLength(0)
                }
                13 -> {}
                else -> line_buffer.append(b.toChar())
            }
        }
    }).let{
        System.setOut(it)
        System.setErr(it)
    }
}