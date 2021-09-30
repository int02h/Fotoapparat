package io.fotoapparat.util

import io.fotoapparat.parameter.FpsRange
import org.junit.Test
import kotlin.test.assertEquals

class CompareFpsRangeByBoundsTest {

    @Test
    fun findMaxFpsRange() {
        // Given
        val fpsRangesList = listOf(
                FpsRange(24000, 24000),
                FpsRange(24000, 30000),
                FpsRange(30000, 30000),
                FpsRange(30000, 36000),
                FpsRange(30000, 34000)
        )

        // When
        val maxRange = fpsRangesList.maxWithOrNull(CompareFpsRangeByBounds)

        // Then
        assertEquals(
                expected = FpsRange(30000, 36000),
                actual = maxRange
        )
    }

    @Test
    fun findMinFpsRange() {
        // Given
        val fpsRangesList = listOf(
                FpsRange(24000, 24000),
                FpsRange(24000, 30000),
                FpsRange(30000, 30000),
                FpsRange(20000, 26000),
                FpsRange(20000, 28000)
        )

        // When
        val minRange = fpsRangesList.minWithOrNull(CompareFpsRangeByBounds)

        // Then
        assertEquals(
                expected = FpsRange(20000, 26000),
                actual = minRange
        )
    }
}
