package com.zingat.rateme

import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by ismailgungor on 24.01.2018.
 */
class CheckCondition {

    fun isConditionsComplete(conditionList: ArrayList<Condition>, eventList: ArrayList<Event>): Boolean {

        val currentEventMap = HashMap<String, Int>()

        eventList
                .map { it.getName() }
                .forEach {
                    if (currentEventMap.containsKey(it)) {
                        val currentValue = currentEventMap[it]!!

                        currentEventMap[it] = currentValue + 1
                    } else {
                        currentEventMap[it] = 1
                    }
                }


        for (condition in conditionList) {

            val currentConditionType = condition.getType()
            val currentConditionValue = condition.getCount()

            if (currentEventMap.containsKey(currentConditionType) && currentEventMap[currentConditionType]!! >= currentConditionValue) {
                return true
            }
        }

        return false
    }

    fun isReminderEnd(reminderValue: Long, reminderDuration: Int): Boolean {

        val currentTime = Calendar.getInstance().timeInMillis

        val diffInMilis = currentTime - reminderValue
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilis)

        return diffInDays > reminderDuration

    }
}