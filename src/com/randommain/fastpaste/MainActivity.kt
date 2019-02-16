package com.randommain.fastpaste

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button

import java.io.OutputStream
import java.io.PrintStream

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

class LoggerOutputStream: OutputStream(){
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
    private val clipboard: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoggerOutputStream().setSystemIO()

        // WebView.setWebContentsDebuggingEnabled(true)

        with(findViewById(R.id.activity_main_button1) as Button){
            setOnClickListener{
                try{
                    setText(R.string.pressed)
                    copyToClipboard()
                    println("copied to clipboard")
                    Log.i(TAG, "now paste")
                    pastePlainText()
                } catch(e: Exception){
                    e.printStackTrace()
                }
            }
        }

        Log.i(TAG, "Hello from Log.i()")
        println("Hello from println()")
    }


    // https://developer.android.com/guide/topics/text/copy-paste
    private fun copyToClipboard(){
        val clip: ClipData = ClipData.newPlainText("simple text", "Hello")
        clipboard.primaryClip = clip
    }

    private fun pastePlainText() = with(clipboard){
        if(!hasPrimaryClip()) return
        if(primaryClipDescription?.hasMimeType("text/plain") == true){
            var item = primaryClip!!.getItemAt(0)
            item.text?.let{
                print("text pasted from clipboard: ")
                println(it)
            }
        }
    }

    private fun pasteContentUriToServer(){
        if(!clipboard.hasPrimaryClip()) return

        val cr = getContentResolver()
        // val clipDescription = clipboard.getPrimaryClipDescription()

        val clip = clipboard.primaryClip
        val pasteUri = clip?.run{
            getItemAt(0).uri
        }
        println("getItemAt(0).uri: $pasteUri")
        pasteUri?.let{
            var uriMimeType = cr.getType(it)
            uriMimeType?.takeIf{
                println("mime: $uriMimeType")
                it == "text/plain"
            }?.apply{
                cr.query(pasteUri, null, null, null, null)?.use{
                    if(it.moveToFirst()){
                        println("column count: ${it.columnCount}")
                        println("row count: ${it.count}")
                    }
                }
            }
        }
    }
}
