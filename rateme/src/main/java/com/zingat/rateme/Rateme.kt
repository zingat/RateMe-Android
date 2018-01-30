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
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.StackingBehavior

/**
 * Created by mustafaolkun on 24/01/2018.
 */
class Rateme() {

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

        mDataHelper = DataHelper(this.mContext)
        mCheckCondition = CheckCondition()
        setPackageName()

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
            val isReminderEnd = mCheckCondition.isReminderEnd(3, reminderValue) // ToDo bunun adı mDuration olmasın çünkü duration farklı bir yerde daha kullanılıcak.

            if (isReminderEnd) {

                showDialog()

            }
        }
    }

    fun startShowProcess() {

        val reminderValue = mDataHelper.getReminder()
        val isReminderEnd = mCheckCondition.isReminderEnd(3, reminderValue)
        if (isReminderEnd) {

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

    fun createDialog(): Rateme {

        this.mDialog = MaterialDialog.Builder(mContext)
                .title(mContext.getString(R.string.rateme_dialog_title))
                .content(mContext.getString(R.string.rateme_dialog_title))
                .cancelable(false)
                .build()

        return this
    }

    fun createDialogWithCustomView(customView: Int): Rateme {

        this.mDialog = MaterialDialog.Builder(mContext)
                .customView(customView, false)
                .cancelable(false)
                .stackingBehavior(StackingBehavior.ALWAYS)
                .build()

        return this
    }


    fun initNativeDialogButtons() {

        setDialogButtonsTextAndTextColor(mDialog)

        setDialogButtonsClickEvents(mDialog)

    }

    fun initCustomDialogButtons(rateButtonBackground: Int, laterButtonBackground: Int, neverButtonBackground: Int) {

        setDialogButtonsTextAndTextColor(mDialog)


        mDialog?.getActionButton(DialogAction.POSITIVE)?.setStackedGravity(GravityEnum.CENTER)
        mDialog?.getActionButton(DialogAction.POSITIVE)?.setStackedSelector(ContextCompat.getDrawable(mContext, rateButtonBackground))

        mDialog?.getActionButton(DialogAction.NEGATIVE)?.setStackedGravity(GravityEnum.CENTER)
        mDialog?.getActionButton(DialogAction.NEGATIVE)?.setStackedSelector(ContextCompat.getDrawable(mContext, laterButtonBackground))


        mDialog?.getActionButton(DialogAction.NEUTRAL)?.setStackedGravity(GravityEnum.CENTER)
        mDialog?.getActionButton(DialogAction.NEUTRAL)?.setStackedSelector(ContextCompat.getDrawable(mContext, neverButtonBackground))


        setDialogButtonsClickEvents(mDialog)


    }

    private fun setDialogButtonsTextAndTextColor(dialog: MaterialDialog?) {

        dialog?.setActionButton(DialogAction.POSITIVE, mContext.getString(R.string.rateme_btn_rate_text))
        dialog?.setActionButton(DialogAction.NEGATIVE, mContext.getString(R.string.rateme_btn_later_text))
        dialog?.setActionButton(DialogAction.NEUTRAL, mContext.getString(R.string.rateme_btn_never_text))

        dialog?.getActionButton(DialogAction.POSITIVE)?.setTextColor(ContextCompat.getColor(mContext, R.color.btn_rate_text_color))
        dialog?.getActionButton(DialogAction.NEGATIVE)?.setTextColor(ContextCompat.getColor(mContext, R.color.btn_later_text_color))
        dialog?.getActionButton(DialogAction.NEUTRAL)?.setTextColor(ContextCompat.getColor(mContext, R.color.btn_never_text_color))

    }

    private fun setDialogButtonsClickEvents(dialog: MaterialDialog?) {


        dialog?.getActionButton(DialogAction.POSITIVE)?.setOnClickListener {
            sendUserToGooglePlay(this.packageName)
        }

        dialog?.getActionButton(DialogAction.NEGATIVE)?.setOnClickListener {
            mDataHelper.deleteEvent("reminder")
            mDataHelper.saveEvent("conditionCompleted")
            remindLater()
            mDialog?.dismiss()
        }

        dialog?.getActionButton(DialogAction.NEUTRAL)?.setOnClickListener {

            mDataHelper.deleteAll()
            mDataHelper.saveEvent("disable")
            mDialog?.dismiss()

        }

    }

    private fun showDialog() {

        mDialog?.show()

    }

}