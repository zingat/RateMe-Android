package com.zingat.rateme.model

/**
 * Created by ismailgungor on 24.01.2018.
 */
class Event(id: Int, name: String, time: Long) {

    private var id: Int
    private var name: String
    private var time: Long

    init {

        this.id = id
        this.name = name
        this.time = time

    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return this.id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getName(): String {
        return this.name
    }

    fun setTime(time: Long) {
        this.time = time
    }

    fun getTime(): Long {
        return this.time
    }

}