package com.ysj.inputname

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ysj.inputname.databinding.ActivityFavBinding

class FavActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}