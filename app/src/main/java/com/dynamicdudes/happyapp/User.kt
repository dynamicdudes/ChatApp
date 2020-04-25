package com.dynamicdudes.happyapp

import java.util.*

data class User(
    val uuid: String,
    val username : String ,
    val profileImageUrl : String
){
    constructor(): this("","","")
}