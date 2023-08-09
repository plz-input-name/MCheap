package com.ysj.inputname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ysj.inputname.databinding.FragmentItemStatBinding

class itemStatFragment: Fragment() {
    lateinit var binding: FragmentItemStatBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemStatBinding.inflate(inflater,container,false)
        return binding.root
    }
}