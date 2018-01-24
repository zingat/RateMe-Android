package com.zingat.rateme

import android.content.Context

/**
 * Created by mustafaolkun on 24/01/2018.
 */
class Rateme() {

    lateinit var mContext: Context
    lateinit var mConditionList: ArrayList<Condition>

    companion object {

        private val mRateme: Rateme by lazy { Rateme() }

        fun getInstance(): Rateme {

            return mRateme
        }

    }

    fun setContext(context: Context): Rateme {
        this.mContext = context
        return this
    }

    fun setConditionList(conditionList: ArrayList<Condition>): Rateme {
        this.mConditionList = conditionList
        return this
    }


}