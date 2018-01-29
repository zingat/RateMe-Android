package com.zingat.rateme

import android.content.Context
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event
import android.content.pm.PackageManager
import android.content.Intent
import android.net.Uri


/**
 * Created by mustafaolkun on 24/01/2018.
 */
class Rateme {

    lateinit var mContext: Context
    var mConditionList: ArrayList<Condition> = ArrayList<Condition>()
    lateinit var mDataHelper: DataHelper
    lateinit var mCheckCondition: CheckCondition

    private var mDuration = 3
    private var packageName = ""

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

    fun isRatemeEnable(): Boolean {
        val disableList = mDataHelper.findByEventName("disable")
        return disableList.size == 0
    }

    private fun init() {

        this.mDataHelper = DataHelper(this.mContext)
        this.mCheckCondition = CheckCondition()
        remindLater()
        setPackageName()

    }

    fun addCondition(type: String, count: Int): Rateme {
        val newCondition = Condition(count, type)
        this.mConditionList.add(newCondition)
        return this
    }

    fun startShowProcess() {

        val reminderValue = mDataHelper.getReminder()
        val isReminderEnd = mCheckCondition.isReminderEnd(3, reminderValue)
        if (!isReminderEnd) {

            val completedList = mDataHelper.findByEventName("conditionCompleted")
            val isConditonCompletedValue = mCheckCondition.isThereConditionCompletedValue(completedList)

            if (!isConditonCompletedValue) {

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

    private fun setPackageName() {

        try {
            val packageInfo = this.mContext.packageManager.getPackageInfo(mContext.packageName, 0)
            this.packageName = packageInfo.packageName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun sendUserToGooglePlay(packageName: String) {
        try {
            this.mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
        } catch (anfe: android.content.ActivityNotFoundException) {
            this.mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
        }

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

    fun createDialogWithCustomView(layout_dialog: Int) {

        this.mDialog = MaterialDialog.Builder(mContext)
                .customView(layout_dialog, false)
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

        mDialog?.getActionButton(DialogAction.POSITIVE)?.setOnClickListener {
            sendUserToGooglePlay(this.packageName)
        }

        mDialog?.getActionButton(DialogAction.NEGATIVE)?.setOnClickListener {
            mDataHelper.deleteEvent("reminder")
            mDataHelper.saveEvent("conditionCompleted")
            remindLater()
            mDialog?.dismiss()
        }

        mDialog?.getActionButton(DialogAction.NEUTRAL)?.setOnClickListener {

            mDataHelper.deleteAll()
            mDataHelper.saveEvent("disable")
            mDialog?.dismiss()

        }

    }

    private fun showDialog() {

        mDialog?.show()

    }

}