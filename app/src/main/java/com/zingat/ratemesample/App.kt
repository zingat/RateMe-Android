package com.zingat.ratemesample

import android.app.Application
import com.zingat.rateme.Rateme

/**
 * Created by ismailgungor on 26.01.2018.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Rateme.getInstance()
                .setContext(applicationContext)
                .addCondition("buton", 3)
                .reminderDuration(0)


    }

}