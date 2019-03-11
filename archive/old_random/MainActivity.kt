package com.basicapp.randompicker

import android.app.Activity
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.basicapp.util.*


/*
https://github.com/fwcd/KotlinLanguageServer/issues/86
574587-20190212-6aafdc37

- standard functions
https://medium.com/@elye.project/
mastering-kotlin-standard-functions-run-with-let-also-and-apply-9cd334b0ef84


https://developer.android.com/reference/android/content/Intent

- nullable Boolean?
https://kotlinlang.org/docs/reference/idioms.html#consuming-a-nullable-boolean
*/
private const val TAG = "MyActivity"



class MainActivity: AppCompatActivity(){
    private val clipboard: ClipboardManager by lazy{
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }
    private val cr: ContentResolver by lazy{
        getContentResolver()
    }

    private val numpad: KeyboardView by lazy{
        findViewById<KeyboardView>(R.id.numpad).initNumpad(this, R.xml.numpad,
            object: KeyboardView.OnKeyboardActionListener{
                override fun onKey(primaryCode: Int, keyCodes: IntArray){
                    println("KeyEvent(pressed: true, " +
                        "keycode: ${primaryCode}, " +
                        "char: ${primaryCode.toChar()}")
                    if(primaryCode.toByte() in "0123456789".toByteArray()){
                        insertAtCursor(getCurrentFocus() as TextView,
                                primaryCode.toChar().toString())

                    } else{
                        println("unexpected keycode")
                    }
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

    // List.contentToString()

    private fun showNumpad(){
        (numpad.getParent() as View).visibility = View.VISIBLE
        numpad.setEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        redirectSystemIOToLog()

        println(getViewTree(
                getWindow().getDecorView().getRootView() as ViewGroup))
        
        // WebView.setWebContentsDebuggingEnabled(true)

        val input1: TextView = findViewById(R.id.activity_main_input1)
        val label1: TextView = findViewById(R.id.activity_main_label1)
        val input2: TextView = findViewById(R.id.activity_main_input2)
        val button1: Button = findViewById(R.id.activity_main_clear_button1)
        val button2: Button = findViewById(R.id.activity_main_clear_button2)
        val button3: Button = findViewById(R.id.activity_main_enter_button)

        input1.setRawInputType(InputType.TYPE_CLASS_PHONE)
        input1.setText("", TextView.BufferType.EDITABLE)

        input2.setRawInputType(InputType.TYPE_CLASS_PHONE)
        input2.setText("", TextView.BufferType.EDITABLE)

        input1.setOnClickListener{
            this.showNumpad()
        }
        input2.setOnClickListener{
            this.showNumpad()
        }

        button1.setOnClickListener{
            input1.setText("", TextView.BufferType.EDITABLE)
            input1.requestFocus()

            try{
                button1.setText(R.string.pressed)
                clipboard.copyPlainText()
                println("copied to clipboard")
                println("now paste")
                Toast.makeText(this@MainActivity,
                        "pasting", Toast.LENGTH_SHORT).show()
                clipboard.pastePlainText()
            } catch(e: Exception){
                e.printStackTrace()
            }
        }

        button2.setOnClickListener{
            input2.setText("", TextView.BufferType.EDITABLE)
            input2.requestFocus()

            try{
                button2.setText(R.string.pressed)
                clipboard.copyContentUri(cr)
                println("copied content uri to clipboard")
                println("now paste")
                clipboard.pasteContentUri(cr)
            } catch(e: Exception){
                e.printStackTrace()
            }
        }

        button3.setOnClickListener{
            val num1 = try{
                input1.getText().toString().toInt()
            } catch(e: Exception){
                println(e)
                1
            }
            val num2 = try{
                input2.getText().toString().toInt()
            } catch(e: Exception){
                println(e)
                10
            }
            val out = (Math.min(num1, num2)..Math.max(num1, num2)).random()
            label1.setText("${out}")
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

