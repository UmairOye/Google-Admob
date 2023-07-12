package com.example.admoballadsimplementation

import android.app.Application
import com.google.android.gms.ads.MobileAds


class AdsApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(
            this
        ) { }
    }
}