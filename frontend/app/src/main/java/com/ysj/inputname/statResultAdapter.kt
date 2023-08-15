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
        bind.thunderPrice.text = data[position].thunderPrice.toString()
        bind.carrotPrice.text = data[position].carrotPrice.toString()
        bind.joongoPrice.text = data[position].joongoPrice.toString()

        if(position+1 == data.size) {
            bind.thunderMod.text = "-"
            bind.carrotMod.text = "-"
            bind.joongoMod.text = "-"
        }
        else {
            val prev = data[position + 1]

            var tmp: Int = (prev.thunderPrice - data[position].thunderPrice)
<<<<<<< HEAD
            if (tmp > 0) {
                bind.thunderMod.setTextColor(Color.RED)
                bind.thunderMod.text = "+"+tmp.toString()
            }
            else if (tmp < 0){
                bind.thunderMod.setTextColor(Color.BLUE)
                bind.thunderMod.text = tmp.toString()
            }
            else{
                bind.thunderMod.text = "-"
            }
            tmp = (data[position].carrotPrice - prev.carrotPrice)
            if (tmp > 0) {
                bind.carrotMod.setTextColor(Color.RED)
                bind.carrotMod.text = "+"+tmp.toString()
            }
            else if (tmp < 0){
                bind.carrotMod.setTextColor(Color.BLUE)
                bind.carrotMod.text = tmp.toString()
            }
            else{
                bind.carrotMod.text = "-"
            }
            tmp = (prev.joongoPrice - data[position].joongoPrice)
            if (tmp > 0) {
                bind.joongoMod.setTextColor(Color.RED)
                bind.joongoMod.text = "+"+tmp.toString()
            }
            else if (tmp < 0){
                bind.joongoMod.setTextColor(Color.BLUE)
                bind.joongoMod.text = tmp.toString()
            }
            else{
                bind.joongoMod.text = "-"
            }
=======
            if (tmp > 0) bind.thunderMod.setTextColor(Color.BLUE)
            else if (tmp < 0) bind.thunderMod.setTextColor(Color.RED)

            bind.thunderMod.text = tmp.toString()

            tmp = (data[position].carrotPrice - prev.carrotPrice)
            if (tmp > 0) bind.carrotMod.setTextColor(Color.BLUE)
            else if (tmp < 0) bind.carrotMod.setTextColor(Color.RED)

            bind.carrotMod.text = tmp.toString()

            tmp = (prev.joongoPrice - data[position].joongoPrice)
            if (tmp > 0) bind.joongoMod.setTextColor(Color.BLUE)
            else if (tmp < 0) bind.joongoMod.setTextColor(Color.RED)

            bind.joongoMod.text = tmp.toString()
>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
        }
        bind.textView8.text = data[position].date
    }
}