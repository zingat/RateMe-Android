package com.zingat.rateme

import com.zingat.rateme.model.Condition
import com.zingat.rateme.model.Event
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy

/**
 * Created by mustafaolkun on 26/01/2018.
 */
class RatemeTest {

    @Spy
    lateinit var rateMe: Rateme

    @Mock
    lateinit var mockDataHelper: DataHelper

    @Mock
    lateinit var mockEventList: ArrayList<Event>

    @Mock
    lateinit var mockCheckCondition: CheckCondition

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        this.rateMe.mDataHelper = this.mockDataHelper
        this.rateMe.mCheckCondition = this.mockCheckCondition
    }

    @Test
    fun startShowProcess_ShouldNotTrigger_IfIsReminderEndReturnFalse() {

        // When
        Mockito.doReturn(false).`when`(this.mockCheckCondition).isReminderEnd(Mockito.anyInt(), Mockito.anyLong())

        this.rateMe.process()

        Mockito.verify(this.mockDataHelper, Mockito.times(1)).getReminder()
        Mockito.verify(this.mockCheckCondition, Mockito.times(1)).isReminderEnd(Mockito.anyInt(), Mockito.anyLong())
        Mockito.verify(this.rateMe, Mockito.never()).showDialog()
        Mockito.verify(this.mockDataHelper, Mockito.never()).findByEventName( Constants.CONDITION_COMPLETED )
        Mockito.verify(this.mockCheckCondition, Mockito.never()).isThereConditionCompletedValue(
                anyList<Event>() as ArrayList<Event>
        )
    }

    @Test
    fun startShowProcess_ShouldTrigger_IfIsReminderEndReturnTrue() {

        // When
        Mockito.doReturn(true).`when`(this.mockCheckCondition).isReminderEnd(Mockito.anyInt(), Mockito.anyLong())
        Mockito.doReturn(this.mockEventList).`when`(this.mockDataHelper).findByEventName(Constants.CONDITION_COMPLETED)
        Mockito.doReturn(true).`when`(this.mockCheckCondition).isThereConditionCompletedValue( this.mockEventList )
        Mockito.doNothing().`when`(this.rateMe).showDialog()

        // Then
        this.rateMe.process()

        // Result
        Mockito.verify(this.rateMe, Mockito.times(1)).showDialog()
        Mockito.verify(this.mockDataHelper, Mockito.never()).getAllEvents()
        Mockito.verify(this.mockCheckCondition, Mockito.never()).isConditionsComplete(
                anyList<Condition>() as ArrayList<Condition>,
                anyList<Event>() as ArrayList<Event>
        )

    }

}