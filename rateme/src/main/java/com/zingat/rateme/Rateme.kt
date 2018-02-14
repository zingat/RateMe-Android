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
@com.zingat.rateme.annotations.RatemeOpen
class Rateme {

    internal var context : Context

    constructor( context: Context ) {
        this.context = context
        init()
    }

    private var mConditionList: ArrayList<Condition> = ArrayList<Condition>()
    private var mDuration = 3
    private var packageName = ""
    private var mDialog: MaterialDialog? = null
    private var rateButtonBackground: Int = 0
    private var laterButtonBackground: Int = 0
    private var neverButtonBackground: Int = 0

    internal lateinit var mDataHelper: DataHelper
    internal lateinit var mCheckCondition: CheckCondition

    companion object : SingletonHolder<Rateme, Context>(::Rateme)

    // Builder Start methods
    fun setConditionList(conditionList: ArrayList<Condition>): Rateme {
        this.mConditionList = conditionList
        return this
    }

    fun addCondition(type: String, count: Int): Rateme {
        val newCondition = Condition(count, type)
        this.mConditionList.add(newCondition)
        return this
    }

    fun remindLater(): Rateme {
        this.mDataHelper.saveEvent(Constants.REMINDER)
        return this
    }

    fun reminderDuration(duration: Int): Rateme {
        this.mDuration = duration
        return this
    }

    fun addEvent(eventName: String): Rateme {

        if (this.mCheckCondition.isRatemeEnable()) {
            this.mDataHelper.saveEvent(eventName)
            this.create()
            this.process()
        }
        return this
    }
    // Builder End methods

    private fun init() {
        this.mDataHelper = DataHelper(this.context)
        this.mCheckCondition = CheckCondition(this.mDataHelper)
        setPackageName()
    }

    internal fun process() {

        val reminderValue = this.mDataHelper.getReminder()
        val isReminderEnd = mCheckCondition.isReminderEnd(this.mDuration, reminderValue)
        if (isReminderEnd) {

            val completedList: ArrayList<Event> = this.mDataHelper.findByEventName(Constants.CONDITION_COMPLETED)
            val isConditonCompletedValue = mCheckCondition.isThereConditionCompletedValue(completedList)

            if (!isConditonCompletedValue) {

                val eventList: ArrayList<Event> = this.mDataHelper.getAllEvents()
                val isConditionComplete = mCheckCondition.isConditionsComplete(this.mConditionList, eventList)

                if (isConditionComplete) {
                    this.showDialog()
                }

            } else {
                this.showDialog()
            }
        }
    }

    private fun setPackageName() {

        try {
            val packageInfo = this.context.packageManager.getPackageInfo(context.packageName, 0)
            this.packageName = packageInfo.packageName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun sendUserToGooglePlay(packageName: String) {
        try {
            this.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
        } catch (anfe: android.content.ActivityNotFoundException) {
            this.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
        }

    }


    private fun create() {

        this.mDialog = MaterialDialog.Builder(this.context)
                .title(context.getString(R.string.rateme_dialog_title))
                .content(context.getString(R.string.rateme_dialog_title))
                .cancelable(false)
                .build()

        this.initNativeDialogButtons()

    }

    private fun create(customView: Int) {

        this.mDialog = MaterialDialog.Builder(this.context)
                .customView(customView, false)
                .cancelable(false)
                .stackingBehavior(StackingBehavior.ALWAYS)
                .build()

    }

    private fun initNativeDialogButtons() {
        setDialogButtonsTextAndTextColor()
        setDialogButtonsClickEvents()
    }

    private fun initCustomDialogButtons(rateButtonBackground: Int, laterButtonBackground: Int, neverButtonBackground: Int): Rateme {

        setDialogButtonsTextAndTextColor()

        this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setStackedGravity(GravityEnum.CENTER)
        this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setStackedSelector(ContextCompat.getDrawable(context, rateButtonBackground))

        this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setStackedGravity(GravityEnum.CENTER)
        this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setStackedSelector(ContextCompat.getDrawable(context, laterButtonBackground))

        this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setStackedGravity(GravityEnum.CENTER)
        this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setStackedSelector(ContextCompat.getDrawable(context, neverButtonBackground))

        setDialogButtonsClickEvents()

        return this
    }

    private fun setDialogButtonsTextAndTextColor() {

        this.mDialog?.setActionButton(DialogAction.POSITIVE, context.getString(R.string.rateme_btn_rate_text))
        this.mDialog?.setActionButton(DialogAction.NEGATIVE, context.getString(R.string.rateme_btn_later_text))
        this.mDialog?.setActionButton(DialogAction.NEUTRAL, context.getString(R.string.rateme_btn_never_text))

        this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setTextColor(ContextCompat.getColor(context, R.color.btn_rate_text_color))
        this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setTextColor(ContextCompat.getColor(context, R.color.btn_later_text_color))
        this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setTextColor(ContextCompat.getColor(context, R.color.btn_never_text_color))

    }

    private fun setDialogButtonsClickEvents() {

        this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setOnClickListener {
            sendUserToGooglePlay(this.packageName)
        }

        this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setOnClickListener {
            this.mDataHelper.deleteEvent(Constants.REMINDER)
            this.mDataHelper.deleteEvent(Constants.CONDITION_COMPLETED)
            this.mDataHelper.saveEvent(Constants.CONDITION_COMPLETED)
            remindLater()
            this.mDialog?.dismiss()
        }

        // TODO seperate the disable protocol.
        this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setOnClickListener {

            this.mDataHelper.deleteAll()
            this.mDataHelper.saveEvent(Constants.DISABLE)
            this.mDialog?.dismiss()

        }

    }

    internal fun showDialog() {
        this.mDialog?.show()
    }

}