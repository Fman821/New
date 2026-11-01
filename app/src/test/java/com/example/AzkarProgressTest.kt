package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AzkarCategory
import com.example.data.model.AzkarItem
import com.example.data.model.AzkarSessionProgress
import com.example.data.repository.AzkarRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AzkarProgressTest {

    private lateinit var context: Context
    private lateinit var repository: AzkarRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.getSharedPreferences("nurul_azkar_prefs", Context.MODE_PRIVATE).edit().clear().commit()
        repository = AzkarRepository(context)
    }

    @Test
    fun testItemCountIncrementAndReset() {
        val catId = "morning"
        val itemId = "01"
        repository.resetItemCount(catId, itemId)
        assertEquals(0, repository.getItemCompletedCount(catId, itemId))

        // Target repeat is 3
        repository.incrementItemCount(catId, itemId, 3)
        assertEquals(1, repository.getItemCompletedCount(catId, itemId))

        repository.incrementItemCount(catId, itemId, 3)
        assertEquals(2, repository.getItemCompletedCount(catId, itemId))

        repository.incrementItemCount(catId, itemId, 3)
        assertEquals(3, repository.getItemCompletedCount(catId, itemId))

        // Reset
        repository.resetItemCount(catId, itemId)
        assertEquals(0, repository.getItemCompletedCount(catId, itemId))
    }

    @Test
    fun testResumeIndexPersistence() {
        val catId = "evening"
        repository.saveResumeIndex(catId, 7)
        assertEquals(7, repository.getResumeIndex(catId))

        repository.saveResumeIndex(catId, 15)
        assertEquals(15, repository.getResumeIndex(catId))
    }

    @Test
    fun testDailySessionProgressCalculation() {
        val items = listOf(
            AzkarItem(id = "01", title = "Item 1", ar = "ذكر 1", am = "አዝካር 1", repeat = 1),
            AzkarItem(id = "02", title = "Item 2", ar = "ذكر 2", am = "አዝካር 2", repeat = 3),
            AzkarItem(id = "03", title = "Item 3", ar = "ذكر 3", am = "አዝካር 3", repeat = 1)
        )
        val category = AzkarCategory(id = "morning", title = "የጥዋት አዝካር", subtitle = "Morning", icon = "sun", items = items)

        val counts = mapOf(
            "morning_01" to 1, // completed
            "morning_02" to 2, // in progress (2 < 3)
            "morning_03" to 0  // not started
        )

        val completed = category.items.count { item ->
            val count = counts["${category.id}_${item.id}"] ?: 0
            count >= item.repeat
        }
        val remaining = category.items.size - completed
        val fraction = completed.toFloat() / category.items.size.toFloat()

        assertEquals(1, completed)
        assertEquals(2, remaining)
        assertEquals(1f / 3f, fraction, 0.001f)
    }

    @Test
    fun testSameItemIdsRemainIndependentAcrossCategories() {
        repository.incrementItemCount("morning", "01", 3)
        repository.incrementItemCount("morning", "01", 3)
        repository.incrementItemCount("evening", "01", 3)

        assertEquals(2, repository.getItemCompletedCount("morning", "01"))
        assertEquals(1, repository.getItemCompletedCount("evening", "01"))

        repository.resetCategoryProgress("morning", listOf("01"))
        assertEquals(0, repository.getItemCompletedCount("morning", "01"))
        assertEquals(1, repository.getItemCompletedCount("evening", "01"))
    }

    @Test
    fun testLegacyFavoritesMigrateToCategoryScopedKeys() {
        repository.toggleFavorite("01")
        val categories = listOf(
            AzkarCategory("morning", "Morning", "", "sun", items = listOf(
                AzkarItem("01", "Morning item", "ذكر", "ዝክር")
            )),
            AzkarCategory("evening", "Evening", "", "moon", items = listOf(
                AzkarItem("01", "Evening item", "ذكر", "ዝክር")
            ))
        )

        repository.migrateFavoriteKeys(categories)
        val favorites = repository.getFavorites()

        assertTrue(favorites.contains("azkar_morning_01"))
        assertTrue(favorites.contains("azkar_evening_01"))
        assertFalse(favorites.contains("01"))

        // Migration is one-time and preserves already migrated keys.
        repository.migrateFavoriteKeys(emptyList())
        assertTrue(repository.getFavorites().contains("azkar_morning_01"))
    }

    @Test
    fun testDailyResetPolicyPreservesFavorites() {
        repository.toggleFavorite("dua_01")
        repository.incrementItemCount("morning", "01", 3)
        repository.checkAndApplyDailyReset()

        assertTrue(repository.getItemCompletedCount("morning", "01") > 0)
        assertTrue(repository.getFavorites().contains("dua_01"))
    }
}
