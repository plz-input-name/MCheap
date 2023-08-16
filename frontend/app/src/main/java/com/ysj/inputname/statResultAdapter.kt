package com.ysj.inputname

import android.graphics.Color
import android.graphics.PostProcessor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ysj.inputname.databinding.ItemrowBinding
import com.ysj.inputname.databinding.NoderowBinding
import java.lang.Math.abs
import java.text.DecimalFormat

class statResultAdapter(val data:ArrayList<nodeData>): RecyclerView.Adapter<statResultAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(item: itemData)
    }
    inner class ViewHolder(val binding: NoderowBinding):RecyclerView.ViewHolder(binding.root){
        init{
        }
    }
    var itemClickListener:OnItemClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NoderowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var bind = holder.binding
        val df = DecimalFormat("#,###")
        bind.thunderPrice.text = df.format(data[position].thunderPrice)+"원"
        bind.carrotPrice.text = df.format(data[position].carrotPrice)+"원"
        bind.joongoPrice.text = df.format(data[position].joongoPrice)+"원"
        var tmp2 = ""
        if(position+1 == data.size) {
            bind.thunderMod.text = "-"
            bind.carrotMod.text = "-"
            bind.joongoMod.text = "-"
        }
        else {
            val prev = data[position + 1]

            var tmp: Int = (data[position].thunderPrice - prev.thunderPrice)
            if (tmp > 0) {
                bind.thunderMod.setTextColor(Color.RED)
                tmp2 = DecimalFormat("#,###").format(tmp)
                bind.thunderMod.text = "▲"+tmp2+"원"
            }
            else if (tmp < 0){
                bind.thunderMod.setTextColor(Color.BLUE)
                tmp2 = DecimalFormat("#,###").format(abs(tmp))
                bind.thunderMod.text = "▼"+tmp2+"원"
            }
            else{
                bind.thunderMod.text = "-"
            }
            tmp = (data[position].carrotPrice - prev.carrotPrice)
            if (tmp > 0) {
                bind.carrotMod.setTextColor(Color.RED)
                tmp2 = DecimalFormat("#,###").format(tmp)
                bind.carrotMod.text = "▲"+tmp2+"원"
            }
            else if (tmp < 0){
                bind.carrotMod.setTextColor(Color.BLUE)
                tmp2 = DecimalFormat("#,###").format(abs(tmp))
                bind.carrotMod.text = "▼"+tmp2+"원"
            }
            else{
                bind.carrotMod.text = "-"
            }
            tmp = (data[position].joongoPrice - prev.joongoPrice)
            if (tmp > 0) {
                bind.joongoMod.setTextColor(Color.RED)
                tmp2 = DecimalFormat("#,###").format(tmp)
                bind.joongoMod.text = "▲"+tmp2+"원"
            }
            else if (tmp < 0){
                bind.joongoMod.setTextColor(Color.BLUE)
                tmp2 = DecimalFormat("#,###").format(abs(tmp))
                bind.joongoMod.text = "▼"+tmp2+"원"
            }
            else{
                bind.joongoMod.text = "-"
            }
        }
        bind.textView8.text = data[position].date
    }
}