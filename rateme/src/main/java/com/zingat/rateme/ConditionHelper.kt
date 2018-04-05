package com.zingat.rateme

import com.zingat.rateme.model.Condition

class ConditionHelper {

    fun findGivenConditionCount( conditionList: ArrayList<Condition>, conditionName : String ): Int {

        return conditionList
                .first { it.getType() == conditionName }
                .getCount()

    }
}