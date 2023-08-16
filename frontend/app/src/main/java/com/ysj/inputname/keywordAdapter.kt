package com.ysj.inputname

import android.graphics.PostProcessor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysj.inputname.databinding.ItemrowBinding
import com.ysj.inputname.databinding.TextrowBinding
import java.text.DecimalFormat

class keywordAdapter(val data:ArrayList<String>): RecyclerView.Adapter<keywordAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(text:String)
    }
    inner class ViewHolder(val binding: TextrowBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.textView2.setOnClickListener {
                itemClickListener?.OnItemClick(data[adapterPosition])
            }
        }
    }
    var itemClickListener:OnItemClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TextrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView2.text = "# "+ data[position] + " "
    }
}