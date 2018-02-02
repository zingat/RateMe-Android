package com.zingat.rateme

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ismailgungor on 24.01.2018.
 *
 * Uses to create standart DB operations with KT.
 */
class EventsDb(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        val DATABASE_NAME = "events_database"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "table_events"
        val EVENT_ID = "event_id"
        val EVENT_NAME = "event_name"
        val EVENT_TIME = "event_time"

    }

    private val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
            + EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + EVENT_NAME + " TEXT,"
            + EVENT_TIME + " LONG" + ")")

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        // Slient is golden
    }


}