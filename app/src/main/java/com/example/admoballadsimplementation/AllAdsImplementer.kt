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
    private var TAG = "AdsTAG"
    private var mInterstitialAd: InterstitialAd? = null

    override  fun setBannerAds(layout: FrameLayout , adView: AdView , context: Activity) {
        layout.addView(adView)
        adView.adUnitId = context.getString(R.string.banner_ad)
        loadBanner(context , adView)
    }


    override fun getAdSize(activity: Activity): AdSize? {
        val display: Display = activity.windowManager.getDefaultDisplay()
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
        if (adSize != null) {
            adView.setAdSize(adSize)
        }
        adView.loadAd(adRequest)
    }


    override fun loadRewardedAds(context: Activity) {
        val adRequest: AdRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context ,
            context.getString(R.string.reward_ad) ,
            adRequest!! ,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    mRewardedAd = rewardedAd
                    rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            super.onAdClicked()
                        }

                        override fun onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                        }

                        override fun onAdImpression() {
                            super.onAdImpression()
                        }

                        override fun onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent()
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
                //                mTextView.setText("The content is unlocked : Answer is 40")
            }
        } else {
            Toast.makeText(activity , "Reward ads is not ready yet" , Toast.LENGTH_SHORT)
                .show()
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
                    // Called when a click is recorded for an ad.
                    Log.d(TAG , "Ad was clicked.")
                }

                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG , "Ad dismissed fullscreen content.")
                    loadInterstitialAd(context)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.e(TAG , "Ad failed to show fullscreen content.")
                    mInterstitialAd = null
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG , "Ad recorded an impression.")
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG , "Ad showed fullscreen content.")
                }
            }
        } else {
            Log.d("TAG" , "The interstitial ad wasn't ready yet.");
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

    override fun showNativeAd(activity: Activity? , frameLayout: FrameLayout) {
        val adView = activity!!.layoutInflater.inflate(R.layout.ad_unified , null) as NativeAdView
        val builder = AdLoader.Builder(activity , activity.getString(R.string.ad_native))
            .forNativeAd { nativeAd: NativeAd ->
                frameLayout.visibility = View.VISIBLE
                populateNativeAdView(nativeAd , adView)
                frameLayout.removeAllViews()
                frameLayout.addView(adView)
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    try {
                        frameLayout.visibility = View.GONE
                        adView.visibility = View.GONE
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            })
        builder.build().loadAd(AdRequest.Builder().build())
    }
}