package com.example.myapp

import android.app.Application

class MySecurityApplication :Application(){

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }

}