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
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class SearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchResultBinding
    lateinit var resAdapter:SearchResultAdapter

    lateinit var transLeft:Animation
    lateinit var transRight:Animation
    val server_url = "http://52.78.214.149:3000"
    var data = "dlCzX-w5lhi2KaQ1JQhvG"
    var size = 10
    var viewList = ArrayList<View>()
    var isStatFragOn = false
<<<<<<< HEAD
    var _selectedSort = 0
    var carrotItems = arrayListOf<itemData>()
    var adaptItems = arrayListOf<itemData>()
=======

>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
    lateinit var body: Document
    lateinit var resData:String

    override fun onResume() {
        binding.progressBar.visibility = View.VISIBLE
<<<<<<< HEAD
        binding.button.visibility = View.VISIBLE
        super.onResume()
    }

=======
        super.onResume()
    }
>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
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
                binding.button.visibility = View.VISIBLE
                binding.floatingActionButton.isClickable = true
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE

        val intent = getIntent()
        val str = intent.getStringExtra("search")
        binding.searchView2.setQuery(str,false)

        binding.floatingActionButton.setOnClickListener {
            if(!isStatFragOn) {
                val fragment = supportFragmentManager.beginTransaction()
                val itemStatFragment = itemStatFragment()
                var bundle = Bundle()
                bundle.putString("search", str)
                itemStatFragment.arguments = bundle
                fragment.replace(R.id.frameLayout, itemStatFragment)
                fragment.commit()
                binding.frameLayout.startAnimation(transLeft)
                binding.frameLayout.visibility = View.VISIBLE
                binding.button.visibility = View.INVISIBLE
                binding.floatingActionButton.setImageResource(R.drawable.leftarrow)
                isStatFragOn = true
            }
            else{
                binding.frameLayout.startAnimation(transRight)
                isStatFragOn = false
                binding.floatingActionButton.isClickable = false
                binding.floatingActionButton.setImageResource(R.drawable.rightarrow)
            }
        }

<<<<<<< HEAD
        binding.button.setOnClickListener {
            //0 : 기본순 1 : 가격 내림차순 2 : 가격 오름차순
            _selectedSort = (_selectedSort + 1)%3
            when(_selectedSort){
                0->{
                    binding.button.text = "기본 순서"
                    carrotItems.sortBy {
                        it.timeStamp
                    }
                    if(resAdapter != null)
                        resAdapter.notifyDataSetChanged()
                }
                1->{
                    binding.button.text = "가격 내림차순"
                    carrotItems.sortByDescending {
                        it.price
                    }
                    if(resAdapter != null)
                        resAdapter.notifyDataSetChanged()
                }
                2->{
                    binding.button.text = "가격 오름차순"
                    carrotItems.sortBy{
                        it.price
                    }
                    if(resAdapter != null)
                        resAdapter.notifyDataSetChanged()
                }
            }
        }

=======
>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
        binding.searchMain.setOnTouchListener (object:OnSwipeTouchListener(this@SearchResultActivity){
            override fun onSwipeLeft() {
                val fragment = supportFragmentManager.beginTransaction()
                val itemStatFragment = itemStatFragment()
                var bundle = Bundle()
                bundle.putString("search", str)
                itemStatFragment.arguments = bundle
                fragment.replace(R.id.frameLayout, itemStatFragment)
                fragment.commit()
                binding.frameLayout.startAnimation(transLeft)
                binding.frameLayout.visibility = View.VISIBLE
                binding.button.visibility = View.INVISIBLE
                isStatFragOn = true
            }
            override fun onSwipeRight() {
                binding.frameLayout.startAnimation(transRight)
            }
        })
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
        binding.searchresview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchresview.addItemDecoration(decoration)
        carrotItems.clear()

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

            var t1 = System.currentTimeMillis()

            CoroutineScope(Dispatchers.IO).async {
                body =
                    Jsoup.connect("https://api.bunjang.co.kr/api/1/find_v2.json?q=" + find + "&order=score&page=0&request_id=2023802153409&stat_device=w&n=80&stat_category_required=1&req_ref=search&version=4")
                        .ignoreContentType(true).get()
            }.await()
            var t2 = System.currentTimeMillis()
            Log.d("bunjang",(t2 - t1).toString())


            var jsonBody = JSONObject(body.text())

            var thunderItems: ArrayList<itemData> = arrayListOf()

            var items = jsonBody.getJSONArray("list")

            if (size > items.length()) {
                size = items.length()
            }
            for (i: Int in 0 until items.length()) {
                val j = items.getJSONObject(i)
                val name = j.getString("name")
                val price = j.getInt("price")
                val imgLink = j.getString("product_image")
                val fav = j.getInt("num_faved")
                val _region = j.getString("location")
                var region: ArrayList<String> = arrayListOf()
                region.add(_region)

                val id = j.getString("pid")
                thunderItems.add(itemData(name, price, imgLink, fav, region, id,"thunder"))
            }

            var joongoItems: ArrayList<itemData> = arrayListOf()
            t1 = System.currentTimeMillis()
            CoroutineScope(Dispatchers.IO).async {
                body =
                    Jsoup.connect("https://web.joongna.com/_next/data/" + data + "/search/" + find + ".json?keyword=" + find + "&page=1")
                        .ignoreContentType(true).get()
            }.await()
            t2 = System.currentTimeMillis()
            Log.d("joongna",(t2 - t1).toString())



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
                val fav = j.getInt("wishCount")
                val _region = j.getJSONArray("locationNames")
                val id = j.getString("seq")

                var region: ArrayList<String> = arrayListOf()
                for (i: Int in 0 until _region.length()) {
                    region.add(_region.getString(i))
                }
                joongoItems.add(itemData(name, price, imgLink, fav, region, id,"joongo"))
            }

            for (j: Int in 1 until 4) {
                t1 = System.currentTimeMillis()
                body =
                    Jsoup.connect("https://www.daangn.com/search/" + find + "/more/flea_market?page=" + j.toString())
                        .ignoreContentType(true).get()
                var temp = body.select("span.article-title")
                val name = arrayListOf<String>()
                val img = arrayListOf<String>()
                val region = arrayListOf<String>()
                val price2 = arrayListOf<Int>()
                val time = arrayListOf<Int>()
                val id = arrayListOf<String>()

<<<<<<< HEAD
                name.addAll(temp.eachText())

                temp = body.select("div.card-photo > img")
                for(k:Int in 0 until temp.size){
                    img.add(temp.attr("src"))
                }

                //Log.d("img", img.toString())
                temp = body.select("body > article > a")
                for(k:Int in 0 until temp.size){
                    id.add(temp.attr("href"))
                }
                //Log.d("id",id.toString())

                temp = body.select("p.article-region-name")
                region.addAll(temp.eachText())

                temp = body.select("p.article-price")
               // Log.d("price",temp.toString())
                for(k:Int in 0 until temp.size) {
                    var price = temp.get(k).text()
=======
                    if (tmp.isEmpty()) {
                        break
                    }
                    body2 = body
                    val title = tmp.text()
                    //imglink
                    var tmp2 =
                        body2.select("body > article:nth-child(" + i.toString() + ") > a > div.card-photo > img")
                            .attr("src").toString()
                    val imglink = tmp2
                    body2 = body
                    //location
                    tmp =
                        body2.select("body > article:nth-child(" + i.toString() + ") > a > div.article-info > p.article-region-name")
                    val location = arrayListOf<String>()
                    location.add(tmp.text())
                    //price
                    body2 = body
                    tmp =
                        body2.select("body > article:nth-child(" + i.toString() + ") > a > div.article-info > p.article-price")
                    var price = tmp.text()
>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
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
                    var _intPrice by Delegates.notNull<Int>()
                    try {
                        _intPrice = price.toInt()
<<<<<<< HEAD
                    } catch (e: Exception) {
                        //e.printStackTrace()
                        _intPrice = 0
                    }
                    price2.add(_intPrice)
                }

                /*for(k:Int in 0 until temp.size) {
                    //getTimeStamp
                    var tmp2 = body.select("body > article:nth-child(" + k.toString() + ") > a")
                        .attr("href")
                    body = Jsoup.connect("https://www.daangn.com" + tmp2).get()
                    tmp2 = body.select("#article-category > time").text()
                    var timestamp = 0L
                    //Log.d("before",tmp2)
                    if (tmp2.contains("시간")) {
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")
                        //  Log.d("시간",tmp2)
                        timestamp = (System.currentTimeMillis() - tmp2.toLong() * 3600000L)
                    } else if (tmp2.contains("분")) {
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")
                        timestamp = (System.currentTimeMillis() - tmp2.toLong() * 60000L)
                    } else if (tmp2.contains("초")) {
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")
                        timestamp = (System.currentTimeMillis() - tmp2.toLong() * 1000L)
                    } else if (tmp2.contains("일")) {
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")

                        //Log.d("일",tmp2)
                        timestamp = (System.currentTimeMillis() - tmp2.toLong() * 86400000L)
                    } else {
                        Log.d("??", tmp2)
                    }
                    val timestamp2 = (timestamp / 1000L).toInt()
                    time.add(timestamp2)
                }*/

                for(z:Int in 0 until price2.size){
                    carrotItems.add(
                        itemData(name.get(z),
                            price2.get(z),
                            img.get(z),
                            0,
                            arrayListOf(region.get(z)),
                            id.get(z),
                            "carrot"
                        )
                    )
                }
                t2 = System.currentTimeMillis()
                Log.d("daangn",(t2-t1).toString())
            }

            binding.progressBar.visibility = View.INVISIBLE
            post(carrotItems, thunderItems, joongoItems, find)

=======
                    }catch (e:Exception){
                        e.printStackTrace()
                        _intPrice = 0
                    }

                    //getTimeStamp
                    tmp2 = body2.select("body > article:nth-child("+i.toString()+") > a").attr("href")
                    body2 = Jsoup.connect("https://www.daangn.com"+tmp2).get()
                    tmp2 = body2.select("#article-category > time").text()
                    var timestamp = 0L
                    //Log.d("before",tmp2)
                    if(tmp2.contains("시간")){
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")
                      //  Log.d("시간",tmp2)
                        timestamp = (System.currentTimeMillis() - tmp2.toLong()*3600000L)
                    }
                    else if(tmp2.contains("분")){
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")
                        timestamp = (System.currentTimeMillis() - tmp2.toLong()*60000L)
                    }
                    else if(tmp2.contains("초")){
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")
                        timestamp = (System.currentTimeMillis() - tmp2.toLong()*1000L)
                    }
                    else if(tmp2.contains("일")){
                        tmp2 = tmp2.replace(("[^0-9]").toRegex(), "")

                        //Log.d("일",tmp2)
                        timestamp = (System.currentTimeMillis() - tmp2.toLong()*86400000L)
                    }
                    else{
                        Log.d("??",tmp2)
                    }
                    val timestamp2 = (timestamp/1000L).toInt()
                    carrotItems.add(itemData(title, _intPrice, imglink, timestamp2, location, "0","carrot"))
                }
            }
            binding.progressBar.visibility = View.INVISIBLE
            post(carrotItems, thunderItems, joongoItems, find)

>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
            carrotItems.addAll(thunderItems)
            carrotItems.addAll(joongoItems)

            carrotItems.sortByDescending{
                it.timeStamp
            }

            runOnUiThread {
                resAdapter = SearchResultAdapter(carrotItems)
                resAdapter.itemClickListener = object:SearchResultAdapter.OnItemClickListener{
                    @SuppressLint("SimpleDateFormat")
                    override fun OnItemClick(item: itemData) {
                        val fragment = supportFragmentManager.beginTransaction()
                        val itemInfoFragment = itemInfoFragment()
                        binding.frameLayout.visibility = View.VISIBLE
                        binding.button.visibility = View.INVISIBLE
                        var bundle = Bundle()
                        bundle.putString("title",item.name)
                        bundle.putInt("price",item.price)
<<<<<<< HEAD
                        when(item.origin){
                            "carrot"->bundle.putString("url","https://daangn.com"+item.id)
                            "thunder"->bundle.putString("url","https://m.bunjang.co.kr/products/"+item.id)
                            "joongo"->bundle.putString("url","https://web.joongna.com/product/"+item.id)
                        }

=======
                        Log.d("loc",item.region.toString())
>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
                        if(item.region.size != 0 ) {
                            bundle.putString("addr", item.region.get(0))
                        }
                        bundle.putString("imglink",item.imgLink)
                        val format = SimpleDateFormat("yyyy.MM.dd")

                        bundle.putString("timestamp",format.format(item.timeStamp))
                        itemInfoFragment.arguments = bundle
                        fragment.replace(R.id.frameLayout, itemInfoFragment).addToBackStack(null)
                        fragment.commit()
                    }
                }
                binding.searchresview.adapter = resAdapter
                resAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun post(carrotItems: ArrayList<itemData>, thunderItems: ArrayList<itemData>, joongoItems: ArrayList<itemData>, keyword:String) {
        val client = OkHttpClient()
        var bodyBuilder = FormBody.Builder()
        val testArr = arrayListOf<Int>()
        //bodyBuilder.add("keyword",keyword)

        for(i:Int in 0 until carrotItems.size){
            bodyBuilder.add("carrot", carrotItems.get(i).price.toString())
            //testArr.add(carrotItems.get(i).price)
        }

        //bodyBuilder.add("carrot",testArr.toString())
        testArr.clear()

        for(i:Int in 0 until thunderItems.size){
            bodyBuilder.add("thunder", thunderItems.get(i).price.toString())
            //testArr.add(thunderItems.get(i).price)
        }
        //bodyBuilder.add("thunder",testArr.toString())
        testArr.clear()

        for(i:Int in 0 until joongoItems.size){
            bodyBuilder.add("joongna", joongoItems.get(i).price.toString())
            //testArr.add(joongoItems.get(i).price)
        }
        //bodyBuilder.add("joongna",testArr.toString())

        var reqBody = bodyBuilder.build()
        val req = Request.Builder().url(server_url+"/statistics/"+keyword).post(reqBody).build()
        client.newCall(req).enqueue(object:Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("POST RES", response.toString())
            }

        })
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
                            //Log.d("test", response.body!!.string())
                            body = response.body!!
                        }
                    }
                })
            }.await()
        }
        //Log.d("test", body.string())
        return body
    }
}
