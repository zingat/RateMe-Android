package com.zingat.rateme

import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * Created by ismailgungor on 24.01.2018.
 *
 * @since 1.0.0
 */
@com.zingat.rateme.annotations.RatemeOpen
class CheckCondition {

    /**
     * Compares the conditions and datas
     * Conditions are provided by developer in their activity by using [Rateme.addCondition]
     * The data got from database by following user's behaviours with [Rateme.addEvent] method
     *
     * @since 1.0.0
     */
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

            if (currentEventMap.containsKey(currentConditionType) && currentEventMap[currentConditionType] == currentConditionValue) {
                return true
            }
        }

        return false
    }

    /**
     * Uses to compare the current time and saved time to database
     * that is saved when user click remind me button.
     * Compares the datas by converting to day value.
     *
     * @see Rateme.remindLater
     * @see Rateme.reminderDuration
     *
     * @since 1.0.0
     */
    fun isReminderEnd(reminderDuration: Int, reminderValue: Long): Boolean {

        val currentTime = Calendar.getInstance().timeInMillis

        val diffInMilis = currentTime - reminderValue
        val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilis)

        return diffInDays > reminderDuration

    }

    fun isThereConditionCompletedValue(eventList: ArrayList<Event>): Boolean {
        return eventList.size > 0
    }

    /**
     * Provides phone current time based on the timezone
     * Uses in different methods to mock in unit tests.
     *
     * @since 1.0.0
     */
    fun getCurrentTime(): Long {
        return Calendar.getInstance().timeInMillis

    }

}