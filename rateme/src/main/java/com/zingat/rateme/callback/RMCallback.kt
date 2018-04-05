package com.zingat.rateme.callback

interface RMCallback {

    fun onEvent( eventName : String, count : Int, which : Int )

}