package com.zingat.rateme

import android.content.Context
import com.zingat.rateme.model.Condition

/**
 * Created by mustafaolkun on 24/01/2018.
 */
class Rateme() {

    lateinit var mContext: Context
    lateinit var mConditionList: ArrayList<Condition>
    private var mDuration = 3

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

    fun addCondition(type: String, count: Int): Rateme {
        val newCondition = Condition(count, type)
        mConditionList.add(newCondition)
        return this
    }

    fun show() {


    }

    fun addEvent(eventName: String): Rateme {

        return this
    }

    fun remindLater(): Rateme {
        return this
    }

    fun delay(delayTime: Long): Rateme {

        return this
    }

    fun reminderDuration(duration: Int): Rateme {
        this.mDuration = duration
        return this
    }

}