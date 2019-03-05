package com.randommain.fastpaste

import android.app.Activity
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast

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
            10 -> { // LF is 10, CR is 13
                Log.i(TAG, line_buffer.toString())
                line_buffer.setLength(0)
            }
            13 -> {}
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


class MainActivity: AppCompatActivity(){
    private val clipboard: ClipboardManager by lazy{
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val cr: ContentResolver by lazy{
        getContentResolver()
    }

    private val numpad: KeyboardView by lazy{
        findViewById<KeyboardView>(R.id.numpad).also{ keyboardView ->
            Keyboard(this, R.xml.numpad).let{ keyboard ->
                keyboardView.setKeyboard(keyboard)
                // keyboardView.setOnKeyboardActionListener{}
            }
            keyboardView.setPreviewEnabled(false)

            // keyboardView.setVerticalOffset

            keyboardView.setOnKeyboardActionListener(
                object: KeyboardView.OnKeyboardActionListener{
                    override fun onKey(primaryCode: Int, keyCodes: IntArray){
                        println("key pressed: ${primaryCode}")
                    }
                    override fun onPress(primaryCode: Int){}
                    override fun onRelease(primaryCode: Int){}
                    override fun onText(text: CharSequence){}
                    override fun swipeDown(){}
                    override fun swipeLeft(){}
                    override fun swipeRight(){}
                    override fun swipeUp(){}
                }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        LoggerOutputStream().setSystemIO()

        println(getViewTree(
                getWindow().getDecorView().getRootView() as ViewGroup))

        // WebView.setWebContentsDebuggingEnabled(true)

        with(findViewById(R.id.activity_main_button1) as Button){
            setOnClickListener{
                try{
                    setText(R.string.pressed)
                    clipboard.copyPlainText()
                    println("copied to clipboard")
                    Log.i(TAG, "now paste")
                    Toast.makeText(this@MainActivity,
                            "pasting", Toast.LENGTH_SHORT).show()
                    clipboard.pastePlainText()
                    numpad.visibility = View.VISIBLE
                    numpad.setEnabled(true)
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

    public override fun onRestart(){
        super.onRestart()
    }

    public override fun onResume(){
        super.onResume()
    }

    public override fun onPause(){
        super.onPause()
    }

    public override fun onStop(){
        super.onStop()
        println("com.randomapp.fastpaste.MainActivity.onStop() invoked")
    }

    public override fun onDestroy(){
        super.onDestroy()
    }
}


fun getViewTree(root: ViewGroup): String{
    fun getViewDesc(v: View): String{
        val res = v.getResources()
        val id = v.getId()
        return "[${v::class.simpleName}]: " + when(true){
            res == null -> "no_resouces"
            id > 0 -> try{
                res.getResourceName(id)
            } catch(e: android.content.res.Resources.NotFoundException){
                "name_not_found"
            }
            else -> "no_id"
        }
    }
    
    val output = StringBuilder(getViewDesc(root))
    for(i in 0 until root.getChildCount()){
        val v = root.getChildAt(i)
        output.append("\n").append(
            if(v is ViewGroup){
                getViewTree(v).prependIndent("  ")
            } else{
                "  " + getViewDesc(v)
            }
        )
    }
    return output.toString()
}