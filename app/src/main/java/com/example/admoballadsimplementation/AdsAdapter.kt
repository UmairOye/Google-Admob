package com.example.admoballadsimplementation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admoballadsimplementation.databinding.RvAdmobBinding

class AdsAdapter(private val mainList: List<AdModel> , private val onclick: AdsListener) :
    RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): AdsViewHolder {
        val binding = RvAdmobBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder , position: Int) {
        holder.bind(mainList[position] , onclick)
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    inner class AdsViewHolder(private val binding: RvAdmobBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: AdModel , onclick: AdsListener) {
            binding.adType.text = item.name
            binding.cdMain.setOnClickListener {
                onclick.onClick(adapterPosition)
            }
        }
    }
}