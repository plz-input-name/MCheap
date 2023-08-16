package com.ysj.inputname

import android.graphics.Color
import android.graphics.ColorSpace.Rgb
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ysj.inputname.databinding.FavrowBinding
import com.ysj.inputname.databinding.TextrowBinding
import kotlinx.coroutines.*
import org.json.JSONArray
import org.jsoup.Jsoup
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FavAdapter(val data:ArrayList<String>): RecyclerView.Adapter<FavAdapter.ViewHolder>() {
    val server_url = "http://52.78.214.149:3000"
    lateinit var activity: FavActivity

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
        activity = parent.context as FavActivity
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val str = data[position]
        holder.binding.textView16.text = str
        val url = server_url+"/statistics/"+str+"?size=10"
        val nodeArr = arrayListOf<nodeData>()
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).async {
                try {
                    val body = Jsoup.connect(url).ignoreContentType(true).get().body()
                    val jarr = JSONArray(body.text())
                    if (jarr.length() < 2) {
                        return@async
                    }
                    for (i: Int in 0 until jarr.length()) {
                        val jobject = jarr.getJSONObject(i)
                        val carrot = jobject.getInt("carrot")
                        val thunder = jobject.getInt("thunder")
                        val joongna = jobject.getInt("joongna")
                        var date = jobject.getString("collected_at")

                        if (str != null)
                            nodeArr.add(nodeData(str, carrot, thunder, joongna, date))
                        else {
                            nodeArr.add(nodeData("none", carrot, thunder, joongna, date))
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }

            }.await()

            nodeArr.sortByDescending {
                LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }

            val c = nodeArr.get(1).carrotPrice - nodeArr.get(0).carrotPrice
            val j = nodeArr.get(1).joongoPrice - nodeArr.get(0).joongoPrice
            val t = nodeArr.get(1).thunderPrice - nodeArr.get(0).thunderPrice

            var c2 = DecimalFormat("#,###").format(c)
            var j2 = DecimalFormat("#,###").format(j)
            var t2 = DecimalFormat("#,###").format(t)

            if (c > 0) {
                c2 = "▲ " + c2
                holder.binding.carrotText.setTextColor(Color.RED)
            } else if (c < 0) {
                c2 = "▼ " + c2
                holder.binding.carrotText.setTextColor(Color.parseColor("#00bde3"))
            } else {
                c2 = "-"
            }

            if (j > 0) {
                j2 = "▲ " + j2
                holder.binding.joongnaText.setTextColor(Color.RED)
            } else if (j < 0) {
                j2 = "▼ " + j2
                holder.binding.joongnaText.setTextColor(Color.parseColor("#00bde3"))
            } else {
                j2 = "-"
            }

            if (t > 0) {
                t2 = "▲ " + t2
                holder.binding.thunderText.setTextColor(Color.RED)
            } else if (t < 0) {
                t2 = "▼ " + t2
                holder.binding.thunderText.setTextColor(Color.parseColor("#00bde3"))
            } else {
                t2 = "-"
            }
            activity.runOnUiThread {
                holder.binding.carrotText.text = c2
                holder.binding.joongnaText.text = j2
                holder.binding.thunderText.text = t2
            }
        }
       // holder.binding.textView2.text = "# "+ data[position] + " "
    }
}