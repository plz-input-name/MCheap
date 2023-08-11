package com.ysj.inputname

import android.graphics.PostProcessor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysj.inputname.databinding.ItemrowBinding

class SearchResultAdapter(val data:ArrayList<itemData>): RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {
    lateinit var parentView: View

    interface OnItemClickListener{
        fun OnItemClick(item: itemData)
    }
    inner class ViewHolder(val binding: ItemrowBinding):RecyclerView.ViewHolder(binding.root){
        init{
            binding.imageView.setOnClickListener {
                itemClickListener?.OnItemClick(data[adapterPosition])
            }
            binding.textView9.setOnClickListener {
                itemClickListener?.OnItemClick(data[adapterPosition])
            }
        }
    }
    var itemClickListener:OnItemClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        parentView = parent.rootView
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(parentView).load(data[position].imgLink).into(holder.binding.imageView)
        holder.binding.textView9.text = data[position].name
        when(data[position].origin){
            "carrot"->{
                holder.binding.imageView2.setImageResource(R.drawable.carrot)
            }
            "thunder"->{
                holder.binding.imageView2.setImageResource(R.drawable.thunder)
            }
            "joongo"->{
                holder.binding.imageView2.setImageResource(R.drawable.joongo)
            }
        }
    }
}