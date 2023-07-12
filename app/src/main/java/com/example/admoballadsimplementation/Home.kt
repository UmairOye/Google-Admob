package com.example.admoballadsimplementation

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.admoballadsimplementation.databinding.FragmentHomeBinding
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class Home : Fragment() , AdsListener, AllAdsInterface by AllAdsImplementer() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdsAdapter
    private lateinit var viewModel: AdsViewModel
    private var adView: AdView? = null



    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater , container , false)
        viewModel = ViewModelProvider(this)[AdsViewModel::class.java]
        viewModel.returnArrayList().observe(viewLifecycleOwner) { adList ->
            if (adList != null) {
                adapter = AdsAdapter(adList , this@Home)
                binding.rvAdsType.adapter = adapter
//                binding.rvAdsType.setHasFixedSize(true)
            }
        }

        adView = AdView(requireContext())
        setBannerAds(binding.include.adViewContainer, adView!!,requireActivity())
        loadRewardedAds(requireActivity())
        loadInterstitialAd(requireActivity())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                loadRewardedAds(requireActivity())
                showRewardAds(requireActivity())
            }

            1 -> {
                showNativeAdDialog()
            }

            2 -> {
                Toast.makeText(requireContext(), "Banner is implemented below, you can check it out", Toast.LENGTH_SHORT).show()
            }

            3 -> {
                showInterstitialAd(requireActivity())
            }
        }

    }

    private fun showNativeAdDialog()
    {
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView: View = inflater.inflate(R.layout.native_dialog , null , false)
        dialogBuilder.setView(dialogView)
        val nativeAd: FrameLayout = dialogView.findViewById(R.id.fl_adplaceholder)
        showNativeAd(requireActivity(),nativeAd)
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
}