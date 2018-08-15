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
import android.os.Build
import android.os.Handler
import android.view.WindowManager
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.StackingBehavior
import com.zingat.rateme.callback.RMCallback
import com.zingat.rateme.callback.RMEventCallback

/**
 * Created by mustafaolkun on 24/01/2018.
 */
@com.zingat.rateme.annotations.RatemeOpen
class Rateme() {

    internal lateinit var context: Context

    constructor(context: Context) : this() {
        this.context = context
        init()
    }

    private var mConditionList: ArrayList<Condition> = ArrayList()
    private var mDuration = 3
    private var packageName = ""
    private var mDialog: MaterialDialog? = null

    private var customView: Int = 0
    private var rateButtonBackground: Int = 0
    private var rateButtonTextColor: Int = R.color.rm_BtnRateTextColor

    private var laterButtonBackground: Int = 0
    private var laterButtonTextColor: Int = R.color.rm_BtnLaterTextColor

    private var neverButtonBackground: Int = 0
    private var neverButtonTextColor: Int = R.color.rm_BtnNeverTextColor

    private var delay: Long = 0
    private var customButtonFlag: Boolean = false
    private val conditionHelper: ConditionHelper = ConditionHelper()
    private lateinit var eventName: String

    internal lateinit var mDataHelper: DataHelper
    internal lateinit var mCheckCondition: CheckCondition

    internal var onShow: RMEventCallback? = null
    internal var onPositive: RMEventCallback? = null
    internal var onNegative: RMEventCallback? = null
    internal var onNeutral: RMEventCallback? = null
    internal var onEvent: RMCallback? = null

    private val STARTED: Int = -1
    private val POSITIVE: Int = 0
    private val NEUTRAL: Int = 1
    private val NEGATIVE: Int = 2

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
            this.eventName = eventName

            this.mDataHelper.saveEvent(eventName)
            this.create()
            this.process()
        }
        return this
    }

    fun custom(customView: Int): Rateme {
        this.customView = customView

        return this
    }

    fun customButton(): Rateme {
        this.customButtonFlag = true
        this.rateButtonBackground = R.drawable.rm_rate_button_background
        this.laterButtonBackground = R.drawable.rm_later_button_background
        this.neverButtonBackground = R.drawable.rm_never_button_background

        return this
    }

    fun customButtonReverse(): Rateme {
        this.customButtonFlag = true
        this.rateButtonBackground = R.drawable.rm_rate_button_reverse_background
        this.laterButtonBackground = R.drawable.rm_later_button_reverse_background
        this.neverButtonBackground = R.drawable.rm_never_button_reverse_background

        this.rateButtonTextColor = R.color.rm_defaultTextColor
        this.laterButtonTextColor = R.color.rm_defaultTextColor
        this.neverButtonTextColor = R.color.rm_defaultTextColor

        return this
    }

    fun delay(miliseconds: Long): Rateme {
        this.delay = miliseconds

        return this
    }

    fun onRateCallback(callback: RMEventCallback): Rateme {
        this.onPositive = callback

        return this
    }

    fun onRemindLaterCallback(callback: RMEventCallback): Rateme {
        this.onNegative = callback

        return this
    }

    fun onDontAskCallback(callback: RMEventCallback): Rateme {
        this.onNeutral = callback

        return this
    }

    fun onShowCallback(callback: RMEventCallback): Rateme {
        this.onShow = callback

        return this
    }

    fun onRMCallback(callback: RMCallback): Rateme {
        this.onEvent = callback

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
                val isConditionComplete = mCheckCondition
                        .setProcessName(this.eventName)
                        .isConditionsComplete(this.mConditionList, eventList)

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

        val builder: MaterialDialog.Builder = MaterialDialog.Builder(this.context)
                .cancelable(false)

        if (this.customView == 0) {
            this.mDialog = builder
                    .title(context.getString(R.string.rateme_dialog_title))
                    .content(context.getString(R.string.rateme_dialog_message))
                    .build()
        } else {
            this.mDialog = builder.customView(customView, false)
                    .stackingBehavior(StackingBehavior.ALWAYS).build()

        }

        this.initDialogButtons()
    }

    private fun initDialogButtons() {

        this.setDialogButtonsTextAndTextColor()
        this.setDialogButtonsClickEvents()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.setCustomSettings()
    }

    private fun setCustomSettings() {
        if (this.customButtonFlag) {
            this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setStackedGravity(GravityEnum.CENTER)
            this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setStackedSelector(ContextCompat.getDrawable(context, this.rateButtonBackground))

            this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setStackedGravity(GravityEnum.CENTER)
            this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setStackedSelector(ContextCompat.getDrawable(context, this.laterButtonBackground))

            this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setStackedGravity(GravityEnum.CENTER)
            this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setStackedSelector(ContextCompat.getDrawable(context, this.neverButtonBackground))
        }
    }

    private fun setDialogButtonsTextAndTextColor() {

        this.mDialog?.setActionButton(DialogAction.POSITIVE, context.getString(R.string.rateme_btn_rate_text))
        this.mDialog?.setActionButton(DialogAction.NEGATIVE, context.getString(R.string.rateme_btn_later_text))
        this.mDialog?.setActionButton(DialogAction.NEUTRAL, context.getString(R.string.rateme_btn_never_text))

        this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setTextColor(ContextCompat.getColor(this.context, this.rateButtonTextColor))
        this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setTextColor(ContextCompat.getColor(this.context, this.laterButtonTextColor))
        this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setTextColor(ContextCompat.getColor(this.context, this.neverButtonTextColor))
    }

    private fun setDialogButtonsClickEvents() {

        this.mDialog?.getActionButton(DialogAction.POSITIVE)?.setOnClickListener {
            this.mDataHelper.saveEvent(Constants.DISABLE)
            this.onPositive?.onEvent()
            this.mDialog!!.dismiss()
            sendUserToGooglePlay(this.packageName)
            this.onEvent?.onEvent(
                    this.eventName,
                    this.conditionHelper.findGivenConditionCount(this.mConditionList, this.eventName),
                    POSITIVE
            )
        }

        this.mDialog?.getActionButton(DialogAction.NEGATIVE)?.setOnClickListener {
            this.mDataHelper.deleteEvent(Constants.REMINDER)
            this.mDataHelper.deleteEvent(Constants.CONDITION_COMPLETED)
            this.mDataHelper.saveEvent(Constants.CONDITION_COMPLETED)
            remindLater()
            this.mDialog?.dismiss()
            this.onNegative?.onEvent()
            this.onEvent?.onEvent(
                    this.eventName,
                    this.conditionHelper.findGivenConditionCount(this.mConditionList, this.eventName),
                    NEGATIVE
            )
        }

        // TODO seperate the disable protocol.
        this.mDialog?.getActionButton(DialogAction.NEUTRAL)?.setOnClickListener {

            this.mDataHelper.deleteAll()
            this.mDataHelper.saveEvent(Constants.DISABLE)
            this.mDialog?.dismiss()
            this.onNeutral?.onEvent()
            this.onEvent?.onEvent(
                    this.eventName,
                    this.conditionHelper.findGivenConditionCount(this.mConditionList, this.eventName),
                    NEUTRAL
            )
        }

    }

    internal fun showDialog() {
        Handler().postDelayed(Runnable {
            try {
                this.onShow?.onEvent()
                this.mDialog?.show()
                this.onEvent?.onEvent(this.eventName, conditionHelper.findGivenConditionCount(this.mConditionList, eventName), STARTED)
            } catch (exception: WindowManager.BadTokenException) {
                exception.printStackTrace()
            }

        }, this.delay)
    }
}