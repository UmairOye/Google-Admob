package com.example.admoballadsimplementation.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.example.admoballadsimplementation.listeners.AllAdsInterface
import com.example.admoballadsimplementation.R
import com.example.admoballadsimplementation.utils.Constants.TAG
import com.example.admoballadsimplementation.utils.Constants.isAdsAvailable
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


class AllAdsImplementer: AllAdsInterface {
    private var mRewardedAd: RewardedAd? = null
    private var mInterstitialAd: InterstitialAd? = null
    private val adRequest: AdRequest = AdRequest.Builder().build()

    override  fun setBannerAds(layout: FrameLayout , adView: AdView , context: Activity) {
        layout.addView(adView)
        adView.adUnitId = context.getString(R.string.banner_ad)
        loadBanner(context , adView)
    }


    override fun getAdSize(activity: Activity): AdSize {
        val display: Display = activity.windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)
        val widthPixels = outMetrics.widthPixels.toFloat()
        val density = outMetrics.density
        val adWidth = (widthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity , adWidth)
    }


    override fun loadBanner(activity: Activity , adView: AdView) {
        val adRequest: AdRequest = AdRequest.Builder()
            .build()
        val adSize = getAdSize(activity)
        adView.setAdSize(adSize)
        adView.loadAd(adRequest)
        adView.adListener = object: AdListener() {
            override fun onAdClicked() {
            }

            override fun onAdClosed() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                isAdsAvailable = false
            }

            override fun onAdImpression() {
            }

            override fun onAdLoaded() {
                isAdsAvailable = true
            }

            override fun onAdOpened() {
            }
        }
    }


    override fun loadRewardedAds(context: Activity) {
        RewardedAd.load(
            context ,
            context.getString(R.string.reward_ad) ,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    mRewardedAd = rewardedAd
                    rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                            loadRewardedAds(context)
                        }
                    }
                }
            })
    }

    override fun showRewardAds(activity: Activity) {
        if (mRewardedAd != null) {
            mRewardedAd!!.show(
                activity
            ) { rewardItem ->
                val amount = rewardItem.amount
                val type = rewardItem.type
            }
            RewardedAd.load(
                activity ,
                activity.getString(R.string.reward_ad) ,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        mRewardedAd = null
                    }

                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        super.onAdLoaded(rewardedAd)
                        mRewardedAd = rewardedAd
                        rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                super.onAdDismissedFullScreenContent()
                                loadRewardedAds(activity)
                            }
                        }
                    }
                })
        } else {
            showToast(activity, activity.getString(R.string.reward_ads_is_not_ready_yet))
        }
    }

    override fun loadInterstitialAd(context: Activity) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context , context.getString(R.string.interstitial_ad) , adRequest ,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    Log.i(TAG , "onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d(TAG , loadAdError.toString())
                    mInterstitialAd = null
                }
            })
    }


    override fun showInterstitialAd(context: Activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.show(context)
            mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    Log.d(TAG , context.getString(R.string.ad_was_clicked))
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG , context.getString(R.string.ad_dismissed_fullscreen_content))
                    loadInterstitialAd(context)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG , context.getString(R.string.ad_failed_to_show_fullscreen_content))
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    Log.d(TAG , context.getString(R.string.ad_recorded_an_impression))
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG , context.getString(R.string.ad_showed_fullscreen_content))
                }
            }
        } else {
            loadInterstitialAd(context)
            Log.d("TAG" , context.getString(R.string.the_interstitial_ad_wasn_t_ready_yet))
        }
    }


    override fun populateNativeAdView(nativeAd: NativeAd , adView: NativeAdView) {
        adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.priceView = adView.findViewById(R.id.ad_price)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.storeView = adView.findViewById(R.id.ad_store)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        (adView.headlineView as TextView?)!!.text = nativeAd.headline
        (adView.bodyView as TextView?)!!.text = nativeAd.body
        (adView.callToActionView as TextView?)!!.text = nativeAd.callToAction
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.GONE
        } else {
            adView.iconView!!.background = nativeAd.icon!!.drawable
            adView.iconView!!.visibility = View.VISIBLE
        }
        if (nativeAd.price == null) {
            adView.priceView!!.visibility = View.VISIBLE
        } else {
            (adView.priceView as TextView?)!!.text = nativeAd.price
            adView.priceView!!.visibility = View.VISIBLE
        }
        if (nativeAd.store == null) {
            adView.storeView!!.visibility = View.VISIBLE
        } else {
            (adView.storeView as TextView?)!!.text = nativeAd.store
            adView.storeView!!.visibility = View.VISIBLE
        }
        if (nativeAd.starRating == null) {
            adView.starRatingView!!.visibility = View.INVISIBLE
        } else {
            (adView.starRatingView as RatingBar?)?.rating = nativeAd.starRating!!.toFloat()
            adView.starRatingView!!.visibility = View.VISIBLE
        }
        if (nativeAd.advertiser == null) {
            adView.advertiserView!!.visibility = View.INVISIBLE
        } else {
            (adView.advertiserView as TextView?)!!.text = nativeAd.advertiser
            adView.advertiserView!!.visibility = View.VISIBLE
        }
        adView.setNativeAd(nativeAd)
    }

    override fun showSmallNativeAd(activity: Activity? , frameLayout: FrameLayout) {
        val adView = activity!!.layoutInflater.inflate(R.layout.native_small, null) as NativeAdView
        val builder = AdLoader.Builder(activity , activity.getString(R.string.ad_native))
            .forNativeAd { nativeAd: NativeAd ->
                populateNativeAdView(nativeAd , adView)
                frameLayout.removeAllViews()
                frameLayout.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    try {
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            })
        builder.build().loadAd(AdRequest.Builder().build())
    }

    override fun showLargeNativeAd(activity: Activity? , frameLayout: FrameLayout) {
        val adView = activity!!.layoutInflater.inflate(R.layout.native_large, null) as NativeAdView
        val builder = AdLoader.Builder(activity , activity.getString(R.string.ad_native))
            .forNativeAd { nativeAd: NativeAd ->
                populateNativeAdView(nativeAd , adView)
                frameLayout.removeAllViews()
                frameLayout.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    try {
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            })
        builder.build().loadAd(AdRequest.Builder().build())
    }


    override fun showSmallNativeAdDialog(activity: Activity)
    {
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.native_small_dialog, null , false)
        dialogBuilder.setView(dialogView)
        val nativeAd: FrameLayout = dialogView.findViewById(R.id.fl_adplaceholder)
        showSmallNativeAd(activity,nativeAd)
        val btnYes: Button = dialogView.findViewById(R.id.btnYes)
        val alertDialog: AlertDialog? = dialogBuilder.create()
        if (alertDialog != null) {
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        if (alertDialog != null) {
            alertDialog.window?.attributes?.windowAnimations =
                R.style.DialogAnimation
        }
        alertDialog?.show()

        btnYes.setOnClickListener {
            alertDialog!!.dismiss()
        }
    }

    override fun showLargeNativeAdDialog(activity: Activity)
    {
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.native_large_dialog, null , false)
        dialogBuilder.setView(dialogView)
        val nativeAd: FrameLayout = dialogView.findViewById(R.id.fl_adplaceholder)
        showSmallNativeAd(activity,nativeAd)
        val btnYes: Button = dialogView.findViewById(R.id.btnYes)
        val alertDialog: AlertDialog? = dialogBuilder.create()
        if (alertDialog != null) {
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        if (alertDialog != null) {
            alertDialog.window?.attributes?.windowAnimations =
                R.style.DialogAnimation
        }
        alertDialog?.show()

        btnYes.setOnClickListener {
            alertDialog!!.dismiss()
        }
    }

    override fun showToast(context: Activity, message: String)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}