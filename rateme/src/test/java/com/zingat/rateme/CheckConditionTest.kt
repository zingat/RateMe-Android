package com.zingat.rateme

import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy


/**
 * Created by mustafaolkun on 26/01/2018.
 */
class CheckConditionTest {

    private val TEST_CONDITION_CONS = "testCondition"
    private val TEST_CONDITION_2_CONS = "testCondition2"

    private val TEST_REMINDER_TIME = 1516395600000 // 20/01/2018
    private val TEST_CURRENT_TIME = 1516654800000 // 23/01/2018

    @Mock
    private lateinit var mockDataHelper: DataHelper

    private lateinit var checkCondition: CheckCondition

    // Fakes
    private val fakeConditionList: ArrayList<Condition> = ArrayList()
    private val fakeEventList: ArrayList<Event> = ArrayList()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        this.fakeEventList.clear()
        this.checkCondition = CheckCondition(this.mockDataHelper)
        this.fakeConditionList.add(Condition(3, TEST_CONDITION_CONS))
        this.fakeConditionList.add(Condition(2, TEST_CONDITION_2_CONS))

        this.fakeEventList.add(Event(1, TEST_CONDITION_CONS, 0))
        this.fakeEventList.add(Event(2, TEST_CONDITION_CONS, 0))
        this.fakeEventList.add(Event(3, TEST_CONDITION_CONS, 0))

        this.fakeEventList.add(Event(4, TEST_CONDITION_2_CONS, 0))
    }


    @Test
    fun isConditionsComplete_ShouldReturnTrue_WhenProcessNameIsCorrect() {

        val result: Boolean = checkCondition
                .setProcessName(TEST_CONDITION_CONS)
                .isConditionsComplete(this.fakeConditionList, this.fakeEventList)

        Assertions.assertThat(result).isTrue()

    }

    @Test
    fun isConditionsComplete_ShouldReturnFalse_WhenGivenProcessNameConditionCountIsNotEnugh() {

        val result: Boolean = checkCondition
                .setProcessName(TEST_CONDITION_2_CONS)
                .isConditionsComplete(this.fakeConditionList, this.fakeEventList)

        Assertions.assertThat(result).isFalse()

    }

    @Test
    fun isConditionsComplete_ShouldReturnFalse_IfProcessNameIsEmpty() {

        val result: Boolean = checkCondition
                .setProcessName("")
                .isConditionsComplete(this.fakeConditionList, this.fakeEventList)

        Assertions.assertThat(result).isFalse()

    }


    @Test
    fun isConditionsComplete_ShouldReturnFalse_IfEventListCountIsMissing() {

        this.fakeEventList.removeAt(0)

        val result: Boolean = checkCondition.isConditionsComplete(this.fakeConditionList, this.fakeEventList)
        Assertions.assertThat(result).isFalse()

    }

    @Test
    fun isConditionsComplete_ShouldReturnFalse_IfEventListCountTrueButNamesAreDifferent() {

        this.fakeEventList.removeAt(0)
        this.fakeEventList.add(Event(4, TEST_CONDITION_2_CONS, 0))

        val result: Boolean = checkCondition.isConditionsComplete(this.fakeConditionList, this.fakeEventList)
        Assertions.assertThat(result).isFalse()

    }

    @Test
    fun isReminderEnd_ShouldReturnTrue() {

        val isReminderEnd: Boolean = this.checkCondition.isReminderEnd(3, TEST_REMINDER_TIME)
        Assertions.assertThat(isReminderEnd).isTrue()
    }

    @Test
    fun isRatemeEnable_ShouldReturnTrue_IfListSizeEqualToZero() {

        this.fakeEventList.clear()
        Mockito.doReturn(this.fakeEventList).`when`(this.mockDataHelper).findByEventName(Constants.DISABLE)
        val ratemeEnable: Boolean = this.checkCondition.isRatemeEnable()
        Assertions.assertThat(ratemeEnable).isTrue()

    }
}