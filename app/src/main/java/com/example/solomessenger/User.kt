package com.example.solomessenger

data class User(
    val id:String="",
    val name:String = "",
    val surname:String = "",
    val age:String="",
    val status:Boolean=false
):java.io.Serializable