package com.example.admoballadsimplementation

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.FrameLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


open class AdsViewModel : ViewModel() {
    val arrayList: MutableLiveData<ArrayList<AdModel>?> = MutableLiveData<ArrayList<AdModel>?>()



    fun returnArrayList(): MutableLiveData<ArrayList<AdModel>?> {
        val list = ArrayList<AdModel>()
        list.add(AdModel("Rewarded"))
        list.add(AdModel("Native"))
        list.add(AdModel("Banner"))
        list.add(AdModel("Interstitial"))

        val arrayList: MutableLiveData<ArrayList<AdModel>?> = MutableLiveData<ArrayList<AdModel>?>()
        arrayList.value = list
        return arrayList
    }




}