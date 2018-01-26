package com.zingat.rateme

import android.content.Context
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event

/**
 * Created by mustafaolkun on 24/01/2018.
 */
class Rateme() {

    lateinit var mContext: Context
    var mConditionList: ArrayList<Condition> = ArrayList<Condition>()
    lateinit var mDataHelper: DataHelper
    lateinit var mCheckCondition: CheckCondition

    private var mDuration = 3
    private var isWorking = true

    private var mDialog: MaterialDialog? = null


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

    private fun init() {

        mDataHelper = DataHelper(this.mContext)
        mCheckCondition = CheckCondition()

    }

    fun addCondition(type: String, count: Int): Rateme {
        val newCondition = Condition(count, type)
        mConditionList.add(newCondition)
        return this
    }

    @Deprecated("deprecated")
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

    fun startShowProcess() {

        val reminderValue = mDataHelper.getReminder()
        val isReminderEnd = mCheckCondition.isReminderEnd(reminderValue, mDuration) // ToDo bunun adı mDuration olmasın çünkü duration farklı bir yerde daha kullanılıcak.

        if (isReminderEnd) {

            if (isWorking) {

                val eventList: ArrayList<Event> = mDataHelper.getAllEvents()
                val isConditionComplete = mCheckCondition.isConditionsComplete(this.mConditionList, eventList)

                if (isConditionComplete) {
                    showDialog()
                }

            } else {

                showDialog()

            }


        }

    }

    fun addEvent(eventName: String): Rateme {
        this.mDataHelper.saveEvent(eventName)
        return this
    }

    private fun remindLater(): Rateme {
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

    fun createDialog() {

        this.mDialog = MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.title))
                .content(mContext.getString(R.string.message))
                .cancelable(false)
                .build()

        initDialogButtons()

    }

    fun createDialogWithCustomView() {

        this.mDialog = MaterialDialog.Builder(mContext)
                .customView(R.layout.layout_dialog, false)
                .cancelable(false)
                .build()

        initDialogButtons()

    }


    private fun initDialogButtons() {

        mDialog?.setActionButton(DialogAction.POSITIVE, mContext.getString(R.string.rate))
        mDialog?.setActionButton(DialogAction.NEGATIVE, mContext.getString(R.string.remind_me_later))
        mDialog?.setActionButton(DialogAction.NEUTRAL, mContext.getString(R.string.dont_ask_again))

        mDialog?.getActionButton(DialogAction.POSITIVE)?.setTextColor(ContextCompat.getColor(mContext, R.color.btn_rate_color))
        mDialog?.getActionButton(DialogAction.NEGATIVE)?.setTextColor(ContextCompat.getColor(mContext, R.color.btn_later_color))
        mDialog?.getActionButton(DialogAction.NEUTRAL)?.setTextColor(ContextCompat.getColor(mContext, R.color.btn_never_color))

    }

    private fun showDialog() {

        mDialog?.show()

    }

}