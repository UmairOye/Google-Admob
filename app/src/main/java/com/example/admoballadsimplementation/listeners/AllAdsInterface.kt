package com.example.admoballadsimplementation.listeners

import android.app.Activity
import android.widget.FrameLayout
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

interface AllAdsInterface {
    fun setBannerAds(layout: FrameLayout , adView: AdView , context: Activity)
    fun getAdSize(activity: Activity): AdSize?
    fun loadBanner(activity: Activity , adView: AdView)
    fun loadRewardedAds(context: Activity)
    fun showRewardAds(activity: Activity)
    fun loadInterstitialAd(context: Activity)
    fun showInterstitialAd(context: Activity)
    fun populateNativeAdView(nativeAd: NativeAd , adView: NativeAdView)
    fun showSmallNativeAd(activity: Activity? , frameLayout: FrameLayout)
    fun showLargeNativeAd(activity: Activity? , frameLayout: FrameLayout)
    fun showSmallNativeAdDialog(activity: Activity)
    fun showLargeNativeAdDialog(activity: Activity)

    fun showToast(context: Activity, message: String)
}