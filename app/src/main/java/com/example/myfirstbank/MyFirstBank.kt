package com.example.myfirstbank

import android.app.Application

class MyFirstBank : Application(){

    companion object{
        lateinit var prefs:Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}