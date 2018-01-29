package com.zingat.rateme

import com.zingat.rateme.model.Event
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
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


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        this.rateMe.mDataHelper = this.mockDataHelper
    }

    @Test
    fun isRatemeEnable_ShouldReturnTrue_IfListSizeEqualZero() {

        // When
        Mockito.doReturn(this.mockEventList).`when`(this.mockDataHelper).findByEventName("disable")

        // Then
        val result: Boolean = rateMe.isRatemeEnable()
        Assertions.assertThat(result).isTrue()

    }

    @Test
    fun isRatemeEnable_ShouldReturnFalse_IfListSizeBiggerThanZero() {

        // When
        Mockito.doReturn(2).`when`(this.mockEventList).size
        Mockito.doReturn(this.mockEventList).`when`(this.mockDataHelper).findByEventName("disable")

        // Then
        val result: Boolean = rateMe.isRatemeEnable()
        Assertions.assertThat(result).isFalse()

    }

}