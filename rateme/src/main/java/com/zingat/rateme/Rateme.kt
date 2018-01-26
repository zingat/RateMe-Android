package com.zingat.rateme

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event

/**
 * Created by mustafaolkun on 24/01/2018.
 */
class Rateme() {

    lateinit var mContext: Context
    lateinit var mConditionList: ArrayList<Condition>
    lateinit var mDataHelper: DataHelper
    lateinit var mCheckCondition: CheckCondition

    private var mDuration = 3


    companion object {

        private val mRateme: Rateme by lazy { Rateme() }

        fun getInstance(): Rateme {

            return mRateme
        }

    }

    fun setContext(context: Context): Rateme {
        this.mContext = context
        init()
        return this
    }

    fun setConditionList(conditionList: ArrayList<Condition>): Rateme {
        this.mConditionList = conditionList
        return this
    }

    fun init() {

        mDataHelper = DataHelper(this.mContext)
        mCheckCondition = CheckCondition()

    }

    fun addCondition(type: String, count: Int): Rateme {
        val newCondition = Condition(count, type)
        mConditionList.add(newCondition)
        return this
    }

    fun show() {

        val eventList: ArrayList<Event> = mDataHelper.getAllEvents()

        val isConditionComplete = mCheckCondition.isConditionsComplete(this.mConditionList, eventList)
        if (isConditionComplete) {
            val reminderValue = mDataHelper.getReminder()
            val isReminderEnd = mCheckCondition.isReminderEnd(reminderValue, mDuration) // ToDo bunun adı mDuration olmasın çünkü duration farklı bir yerde daha kullanılıcak.

            if (isReminderEnd) {

                showDialog()

            }


        }


    }

    fun addEvent(eventName: String): Rateme {
        this.mDataHelper.saveEvent(eventName)
        return this
    }

    fun remindLater(): Rateme {
        this.mDataHelper.saveEvent("reminder")
        return this
    }

    fun delay(delayTime: Long): Rateme {

        return this
    }

    fun reminderDuration(duration: Int): Rateme {
        this.mDuration = duration
        return this
    }

    // Dialog Methods Starts

    fun showDialog() {

        MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.title))
                .content(mContext.getString(R.string.message))
                .positiveText(mContext.getString(R.string.rate))
                .negativeText(mContext.getString(R.string.remind_me_later))
                .neutralText(mContext.getString(R.string.dont_ask_again))
                .show()

    }

}