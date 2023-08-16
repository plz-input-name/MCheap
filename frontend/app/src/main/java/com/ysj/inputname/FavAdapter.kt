package com.ysj.inputname

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ysj.inputname.databinding.FavrowBinding
import com.ysj.inputname.databinding.TextrowBinding

class FavAdapter(val data:ArrayList<String>): RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(text:String)
    }
    inner class ViewHolder(val binding: FavrowBinding):RecyclerView.ViewHolder(binding.root){
        init{
            //.textView2.setOnClickListener {
           //     itemClickListener?.OnItemClick(data[adapterPosition])
           // }
        }
    }
    var itemClickListener:OnItemClickListener?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = FavrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.textView16.text = data[position]
       // holder.binding.textView2.text = "# "+ data[position] + " "
    }
}