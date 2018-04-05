package com.zingat.rateme

import com.zingat.rateme.model.Condition
import junitparams.JUnitParamsRunner
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import junitparams.Parameters

@RunWith(JUnitParamsRunner::class)
class ConditionHelperTest {

    private lateinit var conditionHelper: ConditionHelper
    private val fakeConditionList: ArrayList<Condition> = ArrayList()

    @Before
    fun setUp() {
        this.conditionHelper = ConditionHelper()

        this.fakeConditionList.add(Condition(3, "type1"))
        this.fakeConditionList.add(Condition(4, "type2"))
        this.fakeConditionList.add(Condition(5, "type3"))
        // type1 given twice to validate whether the count returns correct or not.
        this.fakeConditionList.add(Condition(5, "type1"))
    }

    @Test
    @Parameters("type1, 3", "type2, 4", "type3, 5")
    fun findGivenConditionCount_ShouldGiveCorrectCount(typeName: String, expected: Int) {
        val actual = this.conditionHelper.findGivenConditionCount(this.fakeConditionList, typeName)

        Assertions.assertThat(actual)
                .isEqualTo(expected)
    }
}
