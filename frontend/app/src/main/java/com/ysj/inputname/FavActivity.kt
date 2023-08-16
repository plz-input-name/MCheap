package com.ysj.inputname

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ysj.inputname.databinding.ActivityFavBinding

class FavActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        val tmpArr = intent.getStringArrayListExtra("fav")
        val favArr = arrayListOf<String>()
        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.favView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.favView.addItemDecoration(decoration)
        if(tmpArr!=null) {
            for (i: Int in 0 until tmpArr.size){
                favArr.add(tmpArr.get(i))
            }
            val adapter = FavAdapter(favArr)
            binding.favView.adapter = adapter
        }
        setContentView(binding.root)
    }
}