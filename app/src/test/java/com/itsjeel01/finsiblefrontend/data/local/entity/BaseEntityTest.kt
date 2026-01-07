package com.itsjeel01.finsiblefrontend.data.local.entity

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/** Unit tests for BaseEntity with simplified ID-only structure. */
class BaseEntityTest {

    private lateinit var testEntity: TestEntity

    @Before
    fun setUp() {
        testEntity = TestEntity()
    }

    @Test
    fun `test id field defaults to zero`() {
        val freshEntity = TestEntity()
        assertEquals("Default ID should be 0", 0L, freshEntity.id)
    }

    @Test
    fun `test id field is mutable`() {
        testEntity.id = 123L
        assertEquals("ID should be settable", 123L, testEntity.id)
    }

    @Test
    fun `test id field can be updated multiple times`() {
        testEntity.id = 100L
        assertEquals(100L, testEntity.id)

        testEntity.id = 200L
        assertEquals(200L, testEntity.id)

        testEntity.id = 300L
        assertEquals(300L, testEntity.id)
    }

    @Test
    fun `test id field accepts negative values for local IDs`() {
        testEntity.id = -1L
        assertEquals("Negative IDs should be allowed for local entities", -1L, testEntity.id)
    }

    @Test
    fun `test id field accepts large positive values`() {
        testEntity.id = Long.MAX_VALUE
        assertEquals("Large IDs should be supported", Long.MAX_VALUE, testEntity.id)
    }

    @Test
    fun `test id field is public and accessible`() {
        testEntity.id = 999L
        assertEquals("ID should be publicly accessible", 999L, testEntity.id)
    }

    /** Test implementation of BaseEntity for unit testing. */
    private class TestEntity : BaseEntity()
}

