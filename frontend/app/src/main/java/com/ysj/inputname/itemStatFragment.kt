package com.ysj.inputname

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.ysj.inputname.databinding.FragmentItemStatBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.internal.notify
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class itemStatFragment: Fragment() {
    lateinit var binding: FragmentItemStatBinding
    lateinit var activity:SearchResultActivity
    lateinit var bundle:Bundle

    val server_url = "http://52.78.214.149:3000"
    override fun onResume() {
        super.onResume()
        binding.progressBar2.visibility = View.VISIBLE
        if(arguments!= null) {
            bundle = arguments as Bundle
        }
        getData()
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemStatBinding.inflate(inflater,container,false)
        activity = context as SearchResultActivity

        binding.RecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        if(arguments!= null) {
            bundle = arguments as Bundle
        }

        activity.runOnUiThread {
            binding.textView6.text = bundle.getString("search")+"에 대한 검색결과.."
            binding.maxText1.setTextColor(Color.RED)
            binding.minText1.setTextColor(Color.parseColor("#00bde3"))
            binding.maxText1.text = DecimalFormat("#,###").format(bundle.getInt("max"))+"원"
            binding.minText1.text = DecimalFormat("#,###").format(bundle.getInt("min"))+"원"
        }
        getData()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    fun getData(){
        val activity:SearchResultActivity = activity as SearchResultActivity
        val str = arguments?.getString("search","none")
        val url = server_url+"/statistics/"+str+"?size=10"
        val nodeArr = arrayListOf<nodeData>()
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).async {
                try {
                    val body = Jsoup.connect(url).ignoreContentType(true).get().body()
                    val jarr = JSONArray(body.text())
                    if(jarr.length() <= 0){
                        Toast.makeText(activity, "확인 가능한 정보가 없습니다.",Toast.LENGTH_SHORT).show()
                        return@async
                    }
                    for(i:Int in 0 until jarr.length()){
                        val jobject = jarr.getJSONObject(i)
                        val carrot = jobject.getInt("carrot")
                        val thunder = jobject.getInt("thunder")
                        val joongna = jobject.getInt("joongna")
                        var date = jobject.getString("collected_at")

                        if(str!=null)
                            nodeArr.add(nodeData(str, carrot, thunder,joongna,date))
                        else{
                            nodeArr.add(nodeData("none", carrot, thunder,joongna,date))
                        }
                    }
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }

            }.await()

            nodeArr.sortByDescending {
                LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            }


            activity.runOnUiThread {
                var statResultAdapter = statResultAdapter(nodeArr)
                binding.RecyclerView.adapter = statResultAdapter
                statResultAdapter.notifyDataSetChanged()
                binding.progressBar2.visibility = View.INVISIBLE

                if(nodeArr.size>1) {
                    var k = nodeArr.size-1
                    val entry_carrot = mutableListOf<Entry>() //y
                    val entry_joongna = mutableListOf<Entry>()
                    val entry_thunder = mutableListOf<Entry>()
                    val entry_X = mutableListOf<Entry>()
                    lateinit var firstDate:LocalDateTime
                    var cnt = 0
                    while(k >= 0){
                        var it = nodeArr.get(k)
                        if(k == nodeArr.size-1){
                            entry_carrot.add(Entry(cnt.toFloat(), it.carrotPrice.toFloat()))
                            entry_thunder.add(Entry(cnt.toFloat(), it.thunderPrice.toFloat()))
                            entry_joongna.add(Entry(cnt.toFloat(), it.joongoPrice.toFloat()))
                            firstDate = LocalDateTime.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            cnt++
                        }else{
                            val date1 = LocalDateTime.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            val sub = ChronoUnit.SECONDS.between(firstDate, date1)
                            entry_carrot.add(Entry(sub.toFloat(), it.carrotPrice.toFloat()))
                            entry_thunder.add(Entry(sub.toFloat(), it.thunderPrice.toFloat()))
                            entry_joongna.add(Entry(sub.toFloat(), it.joongoPrice.toFloat()))
                        }
                        k--
                    }
                    val lineDataSetC = LineDataSet(entry_carrot,"당근마켓")
                    lineDataSetC.color = Color.rgb(255,196,0)
                    lineDataSetC.setCircleColor(Color.rgb(255,196,0))
                    lineDataSetC.lineWidth = 2f
                    lineDataSetC.setDrawValues(false)
                    val lineDataSetT = LineDataSet(entry_thunder,"번개장터")
                    lineDataSetT.color = Color.rgb(255,67,67)
                    lineDataSetT.setCircleColor(Color.rgb(255,67,67))
                    lineDataSetT.lineWidth = 2f
                    lineDataSetT.setDrawValues(false)
                    val lineDataSetJ = LineDataSet(entry_joongna,"중고나라")
                    lineDataSetJ.color = Color.rgb(15,225,0)
                    lineDataSetJ.setCircleColor(Color.rgb(15,225,0))
                    lineDataSetJ.lineWidth = 2f
                    lineDataSetJ.setDrawValues(false)


                    val chartData = LineData()
                    chartData.addDataSet(lineDataSetC)
                    chartData.addDataSet(lineDataSetT)
                    chartData.addDataSet(lineDataSetJ)

                    binding.carrotChart.xAxis.setDrawLabels(false)
                    binding.carrotChart.data = chartData
                    binding.carrotChart.axisLeft.setDrawLabels(false)
                    binding.carrotChart.axisRight.setDrawLabels(false)
                    binding.carrotChart.description.isEnabled=false
                    binding.carrotChart.invalidate()

                    binding.carrotChk.setOnCheckedChangeListener { compoundButton, isChecked ->
                        lineDataSetC.isVisible = isChecked
                        binding.carrotChart.invalidate()
                    }
                    binding.thunderChk.setOnCheckedChangeListener { compoundButton, isChecked ->
                        lineDataSetT.isVisible = isChecked
                        binding.carrotChart.invalidate()
                    }
                    binding.joongnaChk.setOnCheckedChangeListener { compoundButton, isChecked ->
                        lineDataSetJ.isVisible = isChecked
                        binding.carrotChart.invalidate()
                    }
                }
            }
        }
    }
}