package com.ysj.inputname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ysj.inputname.databinding.FragmentItemStatBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class itemStatFragment: Fragment() {
    lateinit var binding: FragmentItemStatBinding
    lateinit var activity:SearchResultActivity
    lateinit var bundle:Bundle
    override fun onResume() {
        super.onResume()
        binding.progressBar2.visibility = View.VISIBLE
        binding.keywordView.text = bundle?.getString("search")
        getData()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemStatBinding.inflate(inflater,container,false)
        activity = context as SearchResultActivity

        val decoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        binding.RecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.RecyclerView.addItemDecoration(decoration)
        if(arguments!= null) {
            bundle = arguments as Bundle
        }

        activity.runOnUiThread {
            binding.keywordView.text = bundle.getString("search")
        }
        getData()
        return binding.root
    }

    fun getData(){
        val activity:SearchResultActivity = activity as SearchResultActivity
        val str = arguments?.getString("search","none")
        val url = "http://52.78.214.149:3000/statistics/"+str+"?size=10"
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
                        val date = jobject.getString("collected_at").substring(0,10)
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
                LocalDate.parse(it.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            }
            activity.runOnUiThread {

                var statResultAdapter = statResultAdapter(nodeArr)
                binding.RecyclerView.adapter = statResultAdapter
                statResultAdapter.notifyDataSetChanged()
                binding.progressBar2.visibility = View.INVISIBLE
            }
        }
    }
}