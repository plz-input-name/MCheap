package com.ysj.inputname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.ysj.inputname.databinding.FragmentItemInfoBinding

class itemInfoFragment : Fragment(){
    lateinit var binding: FragmentItemInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemInfoBinding.inflate(inflater,container,false)
        binding.button2.setOnClickListener {
            if(activity!=null)
                requireActivity().supportFragmentManager.beginTransaction().remove(this@itemInfoFragment).commit()
        }
        return binding.root
    }
    fun attachBackPressedCallback(){
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(activity!=null)
                    activity!!.supportFragmentManager.beginTransaction().remove(this@itemInfoFragment).commit()
            }
        }
    }

}

