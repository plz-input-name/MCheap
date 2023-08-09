package com.ysj.inputname

import java.sql.Timestamp

data class itemData(
    val name:String, val price:Int, val imgLink:String, val timeStamp: Int, val region:ArrayList<String>, val id:String, val origin:String)
