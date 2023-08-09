package com.ysj.inputname

<<<<<<< HEAD
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import com.ysj.inputname.databinding.ActivityMainBinding
=======
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
>>>>>>> f6b66b9baddb6a0d6fbda9777782bb0f3b6a8e79
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import org.jsoup.Jsoup
<<<<<<< HEAD
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



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
=======
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lateinit var body:org.jsoup.nodes.Document
        lateinit var resData:String
        var find = "맥북"
        var data = "dlCzX-w5lhi2KaQ1JQhvG"

        CoroutineScope(Dispatchers.IO).launch{
            CoroutineScope(Dispatchers.IO).async {
                body = Jsoup.connect("https://web.joongna.com/").post()
            }.await()
            val tmpHead = body.head().toString()
            val a = tmpHead.indexOf("<script src")

            val tmpList = tmpHead.substring(a).split("\n")
            val tmploc = tmpList[tmpList.size-2]
            data = tmploc.substring(("<script src=\"/_next/static/").length+1, tmploc.indexOf("/_ssgManifest.js\" defer></script>"))

            CoroutineScope(Dispatchers.IO).async {
                body = Jsoup.connect("https://api.bunjang.co.kr/api/1/find_v2.json?q="+find+"&order=score&page=0&request_id=2023802153409&stat_device=w&n=100&stat_category_required=1&req_ref=search&version=4")
                    .ignoreContentType(true).get()
            }.await()

            var jsonBody = JSONObject(body.text())
            var thunderItems:ArrayList<itemData> = arrayListOf()
            var items = jsonBody.getJSONArray("list")
            for(i:Int in 0 until items.length()){
                val j =items.getJSONObject(i)
                val name = j.getString("name")
                val price = j.getInt("price")
                val imgLink = j.getString("product_image")
                val time = j.getInt("update_time")
                val _region = j.getString("location")
                var region:ArrayList<String> = arrayListOf()
                region.add(_region)

                val id = j.getString("pid")
                thunderItems.add(itemData(name,price,imgLink,time,region,id))
            }
            Log.d("번개장터",body.text())

            var joongoItems:ArrayList<itemData> = arrayListOf()

            CoroutineScope(Dispatchers.IO).async {
                body = Jsoup.connect("https://web.joongna.com/_next/data/"+data+"/search/"+find+".json?keyword="+find)
                    .ignoreContentType(true).get()
            }.await()
            jsonBody = JSONObject(body.text())
            items = jsonBody.getJSONObject("pageProps")
                .getJSONObject("dehydratedState")
                .getJSONArray("queries")
                .getJSONObject(0)
                .getJSONObject("state")
                .getJSONObject("data")
                .getJSONObject("data")
                .getJSONArray("items")

            for(i:Int in 0 until items.length()){
                val j = items.getJSONObject(i)
                val name = j.getString("title")
                val price = j.getInt("price")
                val imgLink = j.getString("url")
                val _time = j.getString("sortDate")//2023-08-03 16:06:34
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                val time = sdf.parse(_time)?.time?.div(1000)
                val _region = j.getJSONArray("locationNames")
                val id = j.getString("seq")

                var region:ArrayList<String> = arrayListOf()
                for(i:Int in 0 until _region.length()){
                    region.add(_region.getString(i))
                }

                if (time != null) {
                    joongoItems.add(itemData(name,price,imgLink,time.toInt(),region,id))
                }
            }
            Log.d("중고나라",body.text())

            CoroutineScope(Dispatchers.IO).async {
                body = Jsoup.connect("https://www.daangn.com/search/"+find+"/more/flea_market?page=1")
                    .ignoreContentType(true).get()
            }.await()

            Log.d("당근마켓",body.toString())

            val client = OkHttpClient()
            val req = Request.Builder().url("https://web.joongna.com/search/"+find).build()

           /* CoroutineScope(Dispatchers.IO).async {
                client.newCall(req).enqueue(object:Callback{
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("fail1","fail1")
                        e?.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (!response.isSuccessful) {
                            Log.d("fail2","fail2")
                            return
                        }
                        Log.d("test",response.body!!.toString())
                        resData = response.body!!.string()
                    }
                })
            }.await()*/
            for(i:Int in 0 until thunderItems.size){
                Log.d("번개장터",thunderItems.get(i).toString())
            }
            for(i:Int in 0 until joongoItems.size){
                Log.d("중고나라",joongoItems.get(i).toString())
            }
        }
>>>>>>> f6b66b9baddb6a0d6fbda9777782bb0f3b6a8e79
    }
}