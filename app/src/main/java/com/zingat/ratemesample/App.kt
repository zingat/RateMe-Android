package com.zingat.ratemesample

import android.app.Application
import com.facebook.stetho.Stetho
import com.zingat.rateme.Rateme

/**
 * Created by ismailgungor on 26.01.2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        Rateme.getInstance(this)
                .addCondition("button", 3)
                .reminderDuration(3)
    }

}