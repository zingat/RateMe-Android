package com.zingat.ratemesample

import android.app.Application
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.zingat.rateme.Rateme
import com.zingat.rateme.callback.RMCallback
import com.zingat.rateme.callback.RMEventCallback

/**
 * Created by ismailgungor on 26.01.2018.
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this@App)

        Rateme.getInstance(this@App)
                .addCondition("touch_me_event", 3)
                .reminderDuration(3)
                .delay(1000)
                .custom(R.layout.layout_dialog)
                .customButton()
                .onRateCallback(object : RMEventCallback {
                    override fun onEvent() {
                        // TODO
                    }
                })
                .onDontAskCallback(object : RMEventCallback {
                    override fun onEvent() {
                        // TODO
                    }
                })
                .onRemindLaterCallback(object : RMEventCallback {
                    override fun onEvent() {
                        // TODO
                    }
                })
                .onShowCallback(object : RMEventCallback {
                    override fun onEvent() {
                        // TODO
                    }
                })
                .onRMCallback(object : RMCallback {
                    override fun onEvent(eventName: String, count: Int, which: Int) {
                        // TODO
                        println("RMCallback eventName : $eventName")
                        println("RMCallback count : $count")
                        println("RMCallback which : $which")
                    }
                })
    }

}