package com.zingat.rateme

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import com.zingat.rateme.model.Event
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by ismailgungor on 24.01.2018.
 *
 * Completes the CRUD operations for [Rateme] operations.
 * @since 1.0.0
 */
class EventsSql(context: Context) {

    private val dbEvents: EventsDb = EventsDb(context, EventsDb.DATABASE_NAME, null, EventsDb.DATABASE_VERSION)

    /**
     * Inserts a new row with given string value.
     *
     * @see EventsDb.EVENT_NAME
     * @see EventsDb.EVENT_TIME
     *
     * @since 1.0.0
     * @author ismailgungor
     */
    fun save(event: String) {

        val db = dbEvents.writableDatabase

        val values = ContentValues()
        values.put(EventsDb.EVENT_NAME, event)
        values.put(EventsDb.EVENT_TIME, Calendar.getInstance().timeInMillis)

        db.insert(EventsDb.TABLE_NAME, null, values)
        db.close()

    }

    /**
     * @return an event list with given name.
     * @since 1.0.0
     * @author ismailgungor
     */
    fun findByName(eventName: String): ArrayList<Event> {

        val db = dbEvents.readableDatabase
        val eventList = ArrayList<Event>()
        var eventId: Int
        var eventTime: Long
        val selectQuery = "SELECT * FROM " + EventsDb.TABLE_NAME + " WHERE " + EventsDb.EVENT_NAME + "='" + eventName + "'"
        val cursor: Cursor

        try {

            cursor = db.rawQuery(selectQuery, null)

        } catch (error: SQLException) {
            db.close()
            return eventList
        }

        if (cursor.moveToFirst()) {
            do {
                eventId = cursor.getInt(0)
                eventTime = cursor.getLong(2)

                val event = Event(eventId, eventName, eventTime)
                eventList.add(event)

            } while (cursor.moveToNext())
        }

        db.close()

        return eventList
    }

    /**
     * Deletes a single or multiple rows with given name
     *
     * @since 1.0.0
     * @author ismailgungor
     */
    fun delete(eventName: String) {
        val db = dbEvents.writableDatabase
        val whereArgs = arrayOf<String>(eventName)
        db.delete(EventsDb.TABLE_NAME, EventsDb.EVENT_NAME + " =?", whereArgs)
        db.close()
    }

    /**
     * truncate the table
     *
     * @since 1.0.0
     * @author ismailgungor
     */
    fun deleteAll() {

        val db = dbEvents.writableDatabase
        db.delete(EventsDb.TABLE_NAME, null, null);
        db.close();

    }

    /**
     * @return all added events
     *
     * @since 1.0.0
     * @author ismailgungor
     */
    fun getAll(): ArrayList<Event> {

        val db = dbEvents.readableDatabase
        val eventList = ArrayList<Event>()
        var eventId: Int
        var eventName: String
        var eventTime: Long
        val selectQuery = "SELECT * FROM " + EventsDb.TABLE_NAME

        val cursor: Cursor

        try {

            cursor = db.rawQuery(selectQuery, null)

        } catch (error: SQLException) {
            db.close()
            return eventList
        }

        if (cursor.moveToFirst()) {
            do {

                eventId = cursor.getInt(0)
                eventName = cursor.getString(1)
                eventTime = cursor.getLong(2)

                val event = Event(eventId, eventName, eventTime)
                eventList.add(event)

            } while (cursor.moveToNext())
        }

        db.close()

        return eventList
    }


}