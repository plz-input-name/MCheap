package com.ysj.inputname

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import com.ysj.inputname.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.jsoup.Jsoup
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val server_url = "http://52.78.214.149:3000"

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
    }

    override fun onResume() {
        CoroutineScope(Dispatchers.IO).launch {
            getKeyword()
        }
        super.onResume()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.KeywordView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.KeywordView.addItemDecoration(decoration)

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(this@MainActivity, SearchResultActivity::class.java)
                intent.putExtra("search", binding.searchView.query.toString())
                launcher.launch(intent)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        CoroutineScope(Dispatchers.IO).launch {
            getKeyword()
        }
    }
    fun getKeyword(){
        val keywordArr = arrayListOf<String>()
        try {
            val arr = JSONArray(
                Jsoup.connect(server_url + "/keywords?size=5").ignoreContentType(true).get().body()
                    .text()
            )
            for (i: Int in 0 until arr.length()) {
                val jObject = arr.getJSONObject(i)
                keywordArr.add(jObject.getString("keyword"))
            }
            val keywordAdapter = keywordAdapter(keywordArr)
            runOnUiThread {
                keywordAdapter.itemClickListener = object : keywordAdapter.OnItemClickListener {
                    override fun OnItemClick(text: String) {
                        binding.searchView.setQuery(text, true)
                    }
                }
                binding.KeywordView.adapter = keywordAdapter
            }
        }catch(e:java.lang.Exception){
            e.printStackTrace()
        }
    }
}