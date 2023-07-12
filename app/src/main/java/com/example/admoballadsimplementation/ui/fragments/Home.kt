package com.example.admoballadsimplementation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.admoballadsimplementation.R
import com.example.admoballadsimplementation.adapters.AdsAdapter
import com.example.admoballadsimplementation.data.AdsViewModel
import com.example.admoballadsimplementation.databinding.FragmentHomeBinding
import com.example.admoballadsimplementation.listeners.AllAdsInterface
import com.example.admoballadsimplementation.utils.AllAdsImplementer
import com.example.admoballadsimplementation.utils.Constants
import com.google.android.gms.ads.AdView


class Home : Fragment(), AllAdsInterface by AllAdsImplementer() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdsAdapter
    private val viewModel: AdsViewModel by activityViewModels()
    private var adView: AdView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = AdsAdapter()
        binding.rvAdsType.adapter = adapter

        viewModel.returnArrayList().observe(viewLifecycleOwner) { adList ->
            if (adList != null) {
                adapter.submitList(adList)
            }
        }

        adView = AdView(requireContext())
        setBannerAds(binding.include.adViewContainer, adView!!, requireActivity())
        loadRewardedAds(requireActivity())
        loadInterstitialAd(requireActivity())

        adapter.setOnClickListener(listener = object : AdsAdapter.OnClickListener {
            override fun onItemClick(position: Int) {
                try {
                    when (position) {
                        0 -> {
                            if (Constants.isAdsAvailable) {
                                showRewardAds(requireActivity())
                            } else {
                                showToast(
                                    requireActivity(),
                                    getString(R.string.admob_ads_are_not_loaded_yet)
                                )
                            }
                        }


                        1 -> {
                            showSmallNativeAdDialog(requireActivity())
                        }

                        2 -> {
                            showLargeNativeAdDialog(requireActivity())
                        }

                        3 -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.banner_is_implemented_below_you_can_check_it_out),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        4 -> {
                            if (Constants.isAdsAvailable) {
                                showInterstitialAd(requireActivity())
                            } else {
                                showToast(
                                    requireActivity(),
                                    getString(R.string.admob_ads_are_not_loaded_yet)
                                )
                            }
                        }
                    }

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}