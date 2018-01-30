package com.zingat.ratemesample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.Toast
import com.zingat.rateme.Rateme

class MainActivity : AppCompatActivity() {

    lateinit var mRateme: Rateme

    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mRateme = Rateme.getInstance()

        mRateme.setContext(this)
                .addCondition("buton", 3)
                .reminderDuration(0)
                .createDialogWithCustomView(R.layout.layout_dialog)
                .initCustomDialogButtons(R.drawable.rate_button_background,
                        R.drawable.later_button_background,
                        R.drawable.never_button_background)


    }

    fun dokunBana(view: View) {

        count += 1

        if (mRateme.isRatemeEnable()) {

            mRateme.addEvent("buton")
            mRateme.startShowProcess()

        }


        Toast.makeText(this, "$count kere dokunuldu", Toast.LENGTH_SHORT).show()

    }
}
