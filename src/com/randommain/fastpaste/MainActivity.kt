package com.randommain.fastpaste

import android.app.Activity
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button

import java.io.OutputStream
import java.io.PrintStream

import com.randommain.fastpaste.*


/*
574587-20190212-6aafdc37

- standard functions
https://medium.com/@elye.project/
mastering-kotlin-standard-functions-run-with-let-also-and-apply-9cd334b0ef84


https://developer.android.com/reference/android/content/Intent

- nullable Boolean?
https://kotlinlang.org/docs/reference/idioms.html#consuming-a-nullable-boolean
*/
private const val TAG = "MyActivity"


private class LoggerOutputStream: OutputStream(){
    private var line_buffer: StringBuilder = StringBuilder()

    override fun write(b: Int){
        when(b){
            10 -> { // LF, CR is 13
                Log.i(TAG, line_buffer.toString())
                line_buffer.setLength(0)
            }
            else -> line_buffer.append(b.toChar())
        }
    }

    fun setSystemIO(){
        PrintStream(this).let{
            System.setOut(it)
            System.setErr(it)
        }
    }
}


class MainActivity: Activity(){
    private val clipboard: ClipboardManager by lazy{
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val cr: ContentResolver by lazy{
        getContentResolver()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        LoggerOutputStream().setSystemIO()

        // WebView.setWebContentsDebuggingEnabled(true)

        with(findViewById(R.id.activity_main_button1) as Button){
            setOnClickListener{
                try{
                    setText(R.string.pressed)
                    clipboard.copyPlainText()
                    println("copied to clipboard")
                    Log.i(TAG, "now paste")
                    clipboard.pastePlainText()
                } catch(e: Exception){
                    e.printStackTrace()
                }
            }
        }
        with(findViewById(R.id.activity_main_button2) as Button){
            setOnClickListener{
                try{
                    setText(R.string.pressed)
                    clipboard.copyContentUri(cr)
                    println("copied content uri to clipboard")
                    Log.i(TAG, "now paste")
                    clipboard.pasteContentUri(cr)
                } catch(e: Exception){
                    e.printStackTrace()
                }
            }
        }

        Log.i(TAG, "Hello from Log.i()")
        println("Hello from println()")
        println("SDK Level: ${android.os.Build.VERSION.SDK_INT}")
    }

    public override fun onStart(){
        super.onStart()
        println("com.randomapp.fastpaste.MainActivity.onStart() invoked")
    }

    public override fun onStop(){
        super.onStop()
        println("com.randomapp.fastpaste.MainActivity.onStop() invoked")
    }
}
