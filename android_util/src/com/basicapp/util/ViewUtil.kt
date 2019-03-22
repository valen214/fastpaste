package com.basicapp.util


import android.text.Editable
import android.text.Spannable
import android.widget.TextView
import android.view.View
import android.view.ViewGroup

fun insertAtCursor(view: TextView, str: String){
    val start = view.getSelectionStart()
    val end = view.getSelectionEnd()

    view.getText().let{
        when(it){
            is Editable -> it
            is Spannable ->{
                view.setText(it, TextView.BufferType.EDITABLE)
                view.getText() as Editable
            }
            else ->{
                println("unexpected type")
                it as Editable
            }
        }
    }.let{
        it.replace(Math.min(start, end), Math.max(start, end), str)
    }

    // println("current focus: ${getViewDesc(view)}")
}


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

fun getViewTree(root: ViewGroup): String{
    
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

fun android.app.Activity.printRootViewTree(): Unit{
    println(getViewTree(this.getWindow(
            ).getDecorView().getRootView() as ViewGroup))
}