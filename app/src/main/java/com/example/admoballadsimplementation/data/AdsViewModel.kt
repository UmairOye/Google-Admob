package com.example.admoballadsimplementation.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admoballadsimplementation.models.AdModel


open class AdsViewModel : ViewModel() {

    fun returnArrayList(): MutableLiveData<ArrayList<AdModel>?> {
        val list = ArrayList<AdModel>()
        list.add(AdModel("Rewarded"))
        list.add(AdModel("Small Native"))
        list.add(AdModel("Large Native"))
        list.add(AdModel("Banner"))
        list.add(AdModel("Interstitial"))

        val arrayList: MutableLiveData<ArrayList<AdModel>?> = MutableLiveData<ArrayList<AdModel>?>()
        arrayList.value = list
        return arrayList
    }




}