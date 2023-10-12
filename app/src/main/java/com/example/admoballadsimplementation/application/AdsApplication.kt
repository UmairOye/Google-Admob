package com.example.admoballadsimplementation.application

import android.app.Application
import com.google.android.gms.ads.MobileAds


class AdsApplication: Application() {
    private var appOpenManager: AppOpenManager? = null

    override fun onCreate() {
        super.onCreate()
        appOpenManager =  AppOpenManager(this)
        MobileAds.initialize(
            this
        ) { }
    }
}
