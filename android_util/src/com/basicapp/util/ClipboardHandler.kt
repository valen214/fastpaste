package com.basicapp.util


import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.ContentResolver
import android.net.Uri



    // https://developer.android.com/guide/topics/text/copy-paste
fun ClipboardManager.copyPlainText(text: String = "Hello"){
    this.primaryClip = ClipData.newPlainText("simple text", text)
}

fun ClipboardManager.pastePlainText() = with(this){
    if(!hasPrimaryClip()) return
    if(primaryClipDescription?.hasMimeType("text/plain") == true){
        var item = primaryClip!!.getItemAt(0)
        item.text?.let{
            print("text pasted from clipboard: ")
            println(it)
        }
    }
}



fun ClipboardManager.copyContentUri(cr: ContentResolver){
    this.primaryClip = ClipData.newUri(cr, "URI",
            Uri.parse("content://com.randommain.fastpaste/paste1/Hello"
    ))
}
fun ClipboardManager.pasteContentUri(cr: ContentResolver){
    if(!this.hasPrimaryClip()) return

    // val clipDescription = clipboard.getPrimaryClipDescription()

    val clip = this.primaryClip
    val pasteUri = clip?.run{
        getItemAt(0).uri
    }
    println("getItemAt(0).uri: $pasteUri")
    pasteUri?.let{
        var uriMimeType = cr.getType(it)
        println("mime: $uriMimeType")
        uriMimeType?.takeIf{
            it == ClipDescription.MIMETYPE_TEXT_URILIST
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