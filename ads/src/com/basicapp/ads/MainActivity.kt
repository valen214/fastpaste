package com.basicapp.ads

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import com.basicapp.util.*

import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity: AppCompatActivity(){
    private lateinit var drawerLayout: DrawerLayout

    private lateinit var mAdTest: AdView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redirectSystemIOToLog()

        MobileAds.initialize(this, "ca-app-pub-6030416020552943~1124372383")

        drawerLayout = findViewById(R.id.drawer_layout)

        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        var actionbar: ActionBar? = supportActionBar
        actionbar?.apply{
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        
        mAdTest = findViewById(R.id.adTest)
        val adRequestBuilder = AdRequest.Builder()
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        // mAdTest.adSize = AdSize.SMART_BANNER
        // mAdTest.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        // production: "ca-app-pub-6030416020552943/8065185253"
        // https://support.google.com/admob/answer/7356427?hl=en&ref_topic=7384409
        mAdTest.loadAd(adRequestBuilder.build())
        println("ADDED ADS")
        
        printRootViewTree()
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