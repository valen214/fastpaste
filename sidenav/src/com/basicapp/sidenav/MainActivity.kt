package com.basicapp.sidenav

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import com.basicapp.util.*

class MainActivity: AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redirectSystemIOToLog()

        drawerLayout = findViewById(R.id.drawer_layout)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        var actionbar: ActionBar? = supportActionBar
        actionbar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        return when(item.itemId){
            android.R.id.home ->{
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

/*
Process: com.android.inputservice, PID: 1932
E/AndroidRuntime( 1932): java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
*/