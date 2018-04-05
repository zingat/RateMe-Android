package com.zingat.rateme

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        CheckConditionTest::class,
        ConditionHelperTest::class,
        RatemeTest::class
)
class SuiteTest {
}