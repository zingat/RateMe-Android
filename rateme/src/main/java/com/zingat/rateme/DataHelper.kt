package com.zingat.rateme

import android.content.Context
import com.zingat.rateme.model.Event

/**
 * Created by ismailgungor on 24.01.2018.
 */
class DataHelper(context: Context) {

    private val eventsSql: EventsSql = EventsSql(context)


    fun saveEvent(event: String) {
        this.eventsSql.save(event)
    }

    fun getAllEvents(): ArrayList<Event> {
        return this.eventsSql.getAll()
    }

    fun findByEventName(event: String): ArrayList<Event> {
        return this.eventsSql.findByName(event)
    }

    fun getReminder(): Long {

        val reminderList = eventsSql.findByName("reminder")
        if (reminderList.size > 0)
            return reminderList.get(0).getTime()

        return 0
    }

    fun deleteEvent(eventName: String) {
        this.eventsSql.delete(eventName)
    }

    fun deleteAll() {
        this.eventsSql.deleteAll()
    }

}