package com.halallens.flinkis.util

import com.halallens.flinkis.domain.model.TemplateCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TemplateLibraryTest {

    @Test
    fun `built-in templates has 12 entries`() {
        assertEquals(12, Constants.BUILT_IN_TEMPLATES.size)
    }

    @Test
    fun `islamic category has 3 templates`() {
        val islamic = Constants.BUILT_IN_TEMPLATES.filter {
            it.category == TemplateCategory.ISLAMIC
        }
        assertEquals(3, islamic.size)
    }

    @Test
    fun `general category has 9 templates`() {
        val general = Constants.BUILT_IN_TEMPLATES.filter {
            it.category == TemplateCategory.GENERAL
        }
        assertEquals(9, general.size)
    }

    @Test
    fun `ramadan template has 15 routines`() {
        val ramadan = Constants.BUILT_IN_TEMPLATES.first { it.name == "Ramadan Routine" }
        assertEquals(15, ramadan.routines.size)
    }

    @Test
    fun `school day template matches legacy default count`() {
        val schoolDay = Constants.BUILT_IN_TEMPLATES.first { it.name == "School Day" }
        val legacyCount = Constants.getDefaultRoutines(1L).size
        assertEquals(legacyCount, schoolDay.routines.size)
    }

    @Test
    fun `all templates have non-empty routines`() {
        assertTrue(Constants.BUILT_IN_TEMPLATES.all { it.routines.isNotEmpty() })
    }

    @Test
    fun `all built-in templates are marked isBuiltIn`() {
        assertTrue(Constants.BUILT_IN_TEMPLATES.all { it.isBuiltIn })
    }
}
