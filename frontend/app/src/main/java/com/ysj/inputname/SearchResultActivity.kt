package com.ysj.inputname

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.AnimationUtils
import android.widget.SearchView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ysj.inputname.databinding.ActivitySearchResultBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchResultBinding
    lateinit var resAdapter:SearchResultAdapter

    lateinit var transLeft:Animation
    lateinit var transRight:Animation
    val server_url = "https://localhost"
    var data = "dlCzX-w5lhi2KaQ1JQhvG"
    var size = 5
    var viewList = ArrayList<View>()

    lateinit var body: Document
    lateinit var resData:String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transLeft = AnimationUtils.loadAnimation(this,R.anim.translate_left)
        transRight = AnimationUtils.loadAnimation(this,R.anim.translate_right)
        transRight.setAnimationListener(object: AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.frameLayout.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        
        binding = ActivitySearchResultBinding.inflate(layoutInflater)

        binding.searchMain.setOnTouchListener (object:OnSwipeTouchListener(this@SearchResultActivity){
            override fun onSwipeLeft() {

                Log.d("test","LEFT")

                val fragment = supportFragmentManager.beginTransaction()
                fragment.replace(R.id.frameLayout, itemStatFragment())
                fragment.commit()
                binding.frameLayout.startAnimation(transLeft)
                binding.frameLayout.visibility = View.VISIBLE
                binding.button.visibility = View.INVISIBLE
            }
            override fun onSwipeRight() {
                Log.d("test","Right")
                binding.frameLayout.startAnimation(transRight)
            }
        })
        val intent = getIntent()
        val str = intent.getStringExtra("search")
        if(str != null){
            getData(str)
        }

        setContentView(binding.root)

        binding.searchView2.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                lateinit var find:String
                if(query == null){
                    find = ""
                    return false
                }
                find = query.toString()
                getData(find)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }



    fun getData(find:String){
        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val fragment = supportFragmentManager.beginTransaction()
        binding.searchresview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchresview.addItemDecoration(decoration)

        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).async {
                body = Jsoup.connect("https://web.joongna.com/").post()
            }.await()
            val tmpHead = body.head().toString()
            val a = tmpHead.indexOf("<script src")

            val tmpList = tmpHead.substring(a).split("\n")
            val tmploc = tmpList[tmpList.size - 2]
            data = tmploc.substring(
                ("<script src=\"/_next/static/").length + 1,
                tmploc.indexOf("/_ssgManifest.js\" defer></script>")
            )

            CoroutineScope(Dispatchers.IO).async {
                body =
                    Jsoup.connect("https://api.bunjang.co.kr/api/1/find_v2.json?q=" + find + "&order=score&page=0&request_id=2023802153409&stat_device=w&n=200&stat_category_required=1&req_ref=search&version=4")
                        .ignoreContentType(true).get()
            }.await()

            var jsonBody = JSONObject(body.text())

            var thunderItems: ArrayList<itemData> = arrayListOf()

            var items = jsonBody.getJSONArray("list")

            if (size > items.length()) {
                size = items.length()
            }
            for (i: Int in 0 until size) {
                val j = items.getJSONObject(i)
                val name = j.getString("name")
                val price = j.getInt("price")
                val imgLink = j.getString("product_image")
                val time = j.getInt("update_time")
                val _region = j.getString("location")
                var region: ArrayList<String> = arrayListOf()
                region.add(_region)

                val id = j.getString("pid")
                thunderItems.add(itemData(name, price, imgLink, time, region, id,"thunder"))
            }

            var joongoItems: ArrayList<itemData> = arrayListOf()

            CoroutineScope(Dispatchers.IO).async {
                body =
                    Jsoup.connect("https://web.joongna.com/_next/data/" + data + "/search/" + find + ".json?keyword=" + find)
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

            if (size > items.length()) {
                size = items.length()
            }
            for (i: Int in 0 until size) {
                val j = items.getJSONObject(i)
                val name = j.getString("title")
                val price = j.getInt("price")
                val imgLink = j.getString("url")
                val _time = j.getString("sortDate")//2023-08-03 16:06:34
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
                val time = sdf.parse(_time)?.time?.div(1000)
                val _region = j.getJSONArray("locationNames")
                val id = j.getString("seq")

                var region: ArrayList<String> = arrayListOf()
                for (i: Int in 0 until _region.length()) {
                    region.add(_region.getString(i))
                }

                if (time != null) {
                    joongoItems.add(itemData(name, price, imgLink, time.toInt(), region, id,"joongo"))
                }
            }

            var carrotItems = arrayListOf<itemData>()
            for (j: Int in 1 until size / 10) {
                body =
                    Jsoup.connect("https://www.daangn.com/search/" + find + "/more/flea_market?page=" + j.toString())
                        .ignoreContentType(true).get()
                var i = 0
                while (true) {
                    i++
                    //title
                    var tmp =
                        body.select("body > article:nth-child(" + i.toString() + ") > a > div.article-info > div > span.article-title")

                    if (tmp.isEmpty()) {
                        break
                    }
                    val title = tmp.text()
                    //imglink
                    var tmp2 =
                        body.select("body > article:nth-child(" + i.toString() + ") > a > div.card-photo > img")
                            .attr("src").toString()
                    val imglink = tmp2

                    //location
                    tmp =
                        body.select("body > article:nth-child(" + i.toString() + ") > a > div.article-info > p.article-region-name")
                    val location = arrayListOf<String>()
                    location.add(tmp.text())
                    //price
                    tmp =
                        body.select("body > article:nth-child(" + i.toString() + ") > a > div.article-info > p.article-price")
                    var price = tmp.text()
                    if (price.contains(",")) {
                        price = price.replace(",", "")
                    }
                    if (price.contains("만원")) {
                        price = price.replace("만원", "")
                        price = price + "0000"
                    } else if (price.contains("만")) {
                        price = price.replace("만", "")
                        price = price.replace("원", "")
                    } else {
                        price = price.replace("원", "")
                    }
                    if (price.contains(" ")) {
                        price = price.replace(" ", "")
                    }
                    val _intPrice = price.toInt()
                    carrotItems.add(itemData(title, _intPrice, imglink, 0, location, "0","carrot"))
                }
            }
            post(carrotItems, thunderItems, joongoItems, find, size)

            carrotItems.addAll(thunderItems)
            carrotItems.addAll(joongoItems)

            Log.d("test",carrotItems.toString())
            runOnUiThread {
                resAdapter = SearchResultAdapter(carrotItems)
                resAdapter.itemClickListener = object:SearchResultAdapter.OnItemClickListener{
                    override fun OnItemClick(item: itemData) {
                        binding.frameLayout.visibility = View.VISIBLE
                        binding.button.visibility = View.INVISIBLE
                        fragment.replace(R.id.frameLayout, itemInfoFragment())
                        fragment.commit()
                    }
                }
                binding.searchresview.adapter = resAdapter
                resAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun post(carrotItems: ArrayList<itemData>, thunderItems: ArrayList<itemData>, joongoItems: ArrayList<itemData>, keyword:String, size:Int) {
        val client = OkHttpClient()
        var reqBodyBuilder = FormBody.Builder()
            .add("keyword",keyword)

        for(i:Int in 0 until carrotItems.size){
            reqBodyBuilder.add("carrot["+i.toString()+"]",carrotItems.get(i).price.toString())
        }
        for(i:Int in 0 until thunderItems.size){
            reqBodyBuilder.add("thunder["+i.toString()+"]",thunderItems.get(i).price.toString())
        }
        for(i:Int in 0 until joongoItems.size){
            reqBodyBuilder.add("joongo["+i.toString()+"]",joongoItems.get(i).price.toString())
        }
        var reqBody = reqBodyBuilder.build()
        val req = Request.Builder().url(server_url+"/statistics/"+keyword+"?size="+size).post(reqBody).build()
    }

    fun get(keyword:String, size:Int): ResponseBody {
        val client = OkHttpClient()
        val req = Request.Builder()
            .url(server_url+"/statistics/"+keyword+"?size="+size)
            .build()
        lateinit var body: ResponseBody
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.IO).async {
                client.newCall(req).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!response.isSuccessful) throw IOException("Unexpected code")
                            Log.d("test", response.body!!.string())
                            body = response.body!!
                        }
                    }
                })
            }.await()
        }
        Log.d("test", body.string())
        return body
    }
}
