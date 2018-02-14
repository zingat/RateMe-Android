package com.zingat.rateme

import android.content.Context

/**
 * Created by mustafaolkun on 14/02/2018.
 */
open class SingletonHolder<out T : Rateme, in A : Context>(creator: (A) -> T) {

    private var creator: ((A) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val i = instance
        if (i != null) {
            i.context = arg
            return i
        }
        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
}