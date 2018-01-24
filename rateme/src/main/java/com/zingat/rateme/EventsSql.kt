package com.zingat.rateme

import android.content.Context

/**
 * Created by ismailgungor on 24.01.2018.
 */
class EventsSql(context: Context) {

    private val dbEvents: EventsDb = EventsDb(context, EventsDb.DATABASE_NAME, null, EventsDb.DATABASE_VERSION)

    fun findByName(eventName: String): ArrayList<String> {

        return ArrayList<String>()
    }

    fun save(event: String) {

    }

    fun getAll(): ArrayList<String> {

        return ArrayList<String>()
    }


}