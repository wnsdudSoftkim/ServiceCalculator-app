package com.junyoung.day

import java.io.Serializable

class FireData(
    val title :String? =null,
    val description:String? = null,
    val id  :String? = null,
    val totalserve : String?="000",
    val hasserve:String?="0",
    val leftserve:String?="0",
    val nickname:String?="군인",
    val progressbar : String?="0",
    val myname:String?=null,
    val imagename:String?=null

):Serializable {}

