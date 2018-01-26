package com.zingat.rateme

import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test


/**
 * Created by mustafaolkun on 26/01/2018.
 */
class CheckConditionTest {

    private val TEST_CONDITION_CONS = "testCondition"
    private val TEST_CONDITION_2_CONS = "testCondition2"

    private lateinit var checkCondition : CheckCondition

    // Fakes
    private val fakeConditionList : ArrayList<Condition> = ArrayList()
    private val fakeEventList : ArrayList<Event> = ArrayList()

    @Before
    fun setUp() {
        this.checkCondition = CheckCondition()

        this.fakeConditionList.add( Condition( 3, TEST_CONDITION_CONS ) )

        this.fakeEventList.add(Event( 1, TEST_CONDITION_CONS, 0 ))
        this.fakeEventList.add(Event( 2, TEST_CONDITION_CONS, 0 ))
        this.fakeEventList.add(Event( 3, TEST_CONDITION_CONS, 0 ))
    }


    @Test
    fun isConditionsComplete_ShouldReturnTrue() {

        val result : Boolean = checkCondition.isConditionsComplete( this.fakeConditionList, this.fakeEventList )
        Assertions.assertThat( result ).isTrue()

    }

    @Test
    fun isConditionsComplete_ShouldReturnFalse_IfEventListCountIsMissing() {

        this.fakeEventList.removeAt( 0 )

        val result : Boolean = checkCondition.isConditionsComplete( this.fakeConditionList, this.fakeEventList )
        Assertions.assertThat( result ).isFalse()

    }

    @Test
    fun isConditionsComplete_ShouldReturnFalse_IfEventListCountTrueButNamesAreDifferent() {

        this.fakeEventList.removeAt( 0 )
        this.fakeEventList.add( Event( 4, TEST_CONDITION_2_CONS, 0 ) )

        val result : Boolean = checkCondition.isConditionsComplete( this.fakeConditionList, this.fakeEventList )
        Assertions.assertThat( result ).isFalse()

    }


}