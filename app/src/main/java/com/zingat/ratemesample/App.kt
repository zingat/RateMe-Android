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
                .custom(R.layout.layout_dialog)
                .delay(2500)
                .customButton()
                .onRMCallback( object : RMCallback {
                    override fun onEvent(eventName: String, count: Int, which: Int) {
                        var eventData = "$eventName showed on after $count condition completed."
                        if( which > -1 ){
                            eventData += " Clicked event is $which"
                        }
                        Toast.makeText(this@App, eventData, Toast.LENGTH_SHORT).show()
                    }
                })
    }

}