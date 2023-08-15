package com.ysj.inputname

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ysj.inputname.databinding.FragmentItemInfoBinding
import java.text.DecimalFormat

class itemInfoFragment : Fragment(){
    lateinit var binding: FragmentItemInfoBinding
    lateinit var SRactivity: SearchResultActivity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemInfoBinding.inflate(inflater,container,false)
        SRactivity = activity as SearchResultActivity
        val bundle = arguments
        val imgLink = bundle?.getString("imglink")
        val title = bundle?.getString("title")
        val price = bundle?.getInt("price", 0)
        val addr = bundle?.getString("addr", "미표기")
<<<<<<< HEAD
        val link = bundle?.getString("url")

        val makedPrice = "₩"+DecimalFormat("#,###").format(price)
        SRactivity.runOnUiThread {
            binding.addrView.text = addr
            binding.nameView.text = title
            binding.priceView.text = makedPrice

            Glide.with(this@itemInfoFragment).load(imgLink).into(binding.imageView3)
        }
        binding.button3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(intent)
        }
=======

        val makedPrice = DecimalFormat("#,###").format(price)
        SRactivity.runOnUiThread {
            binding.addrView.text = addr
            binding.nameView.text = title
            binding.priceView.text = price.toString()

            Glide.with(this@itemInfoFragment).load(imgLink).into(binding.imageView3)
        }
>>>>>>> a718141d23ac95c2c63afe1393348f93ccb454e2
        binding.button2.setOnClickListener {
            if(activity!=null) {
                (activity as SearchResultActivity).binding.button.visibility = View.VISIBLE
                requireActivity().supportFragmentManager.beginTransaction()
                    .remove(this@itemInfoFragment).commit()
            }
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

