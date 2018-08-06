package com.zingat.ratemesample

import android.app.Application
import android.widget.Toast
import com.facebook.stetho.Stetho
import com.zingat.rateme.Rateme
import com.zingat.rateme.callback.RMCallback

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
                .delay(2500)
    }

}