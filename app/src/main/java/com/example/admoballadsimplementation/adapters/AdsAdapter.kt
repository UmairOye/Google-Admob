package com.example.admoballadsimplementation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.admoballadsimplementation.databinding.RvAdmobBinding
import com.example.admoballadsimplementation.models.AdModel

class AdsAdapter :
    RecyclerView.Adapter<AdsAdapter.AdsViewHolder>() {
    private var listener: OnClickListener? = null
    private var mainList: List<AdModel> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup , viewType: Int): AdsViewHolder {
        val binding = RvAdmobBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        holder.bind(mainList[position])
        holder.cdMain.setOnClickListener { listener?.onItemClick(position) }
    }

    override fun getItemCount(): Int {
        return mainList.size
    }

    inner class AdsViewHolder(private val binding: RvAdmobBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val cdMain = binding.cdMain


        fun bind(item: AdModel) {
            binding.adType.text = item.name
        }
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun submitList(list: List<AdModel>)
    {
        this.mainList = list
        notifyDataSetChanged()
    }
}