package com.zingat.rateme.model

/**
 * Created by ismailgungor on 24.01.2018.
 */
class Condition(count: Int, type: String) {

    private var mCount = count
    private var mType = type

    fun setCount(count: Int) {
        this.mCount = count
    }

    fun getCount(): Int {

        return this.mCount
    }

    fun setType(type: String) {
        this.mType = type
    }

    fun getType(): String {
        return this.mType
    }

}