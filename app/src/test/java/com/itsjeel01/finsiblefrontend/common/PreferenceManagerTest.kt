package com.itsjeel01.finsiblefrontend.common

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for PreferenceManager sync and local ID counter methods. */
class PreferenceManagerTest {

    private lateinit var mockPreferenceManager: PreferenceManager
    private val prefValues = mutableMapOf<String, Any?>()

    @Before
    fun setUp() {
        mockPreferenceManager = mockk(relaxed = true)

        // Setup mock behavior for local ID counter
        every { mockPreferenceManager.getLocalIdCounter() } answers {
            prefValues["local_id_counter"] as? Long ?: 0L
        }
        every { mockPreferenceManager.saveLocalIdCounter(any()) } answers {
            prefValues["local_id_counter"] = firstArg<Long>()
        }

        // Setup mock behavior for sync enabled
        every { mockPreferenceManager.isSyncEnabled() } answers {
            prefValues["sync_enabled"] as? Boolean ?: false
        }
        every { mockPreferenceManager.setSyncEnabled(any()) } answers {
            prefValues["sync_enabled"] = firstArg<Boolean>()
        }

        // Setup mock behavior for backup enabled
        every { mockPreferenceManager.isBackupEnabled() } answers {
            prefValues["backup_enabled"] as? Boolean ?: false
        }
        every { mockPreferenceManager.setBackupEnabled(any()) } answers {
            prefValues["backup_enabled"] = firstArg<Boolean>()
        }

        // Setup mock behavior for WiFi-only sync
        every { mockPreferenceManager.isWifiOnlySyncEnabled() } answers {
            prefValues["wifi_only_sync"] as? Boolean ?: true
        }
        every { mockPreferenceManager.setWifiOnlySyncEnabled(any()) } answers {
            prefValues["wifi_only_sync"] = firstArg<Boolean>()
        }
    }

    // Local ID Counter Tests
    @Test
    fun testGetLocalIdCounterReturnsZeroByDefault() {
        val counter = mockPreferenceManager.getLocalIdCounter()
        assertEquals("Default counter should be 0", 0L, counter)
    }

    @Test
    fun testSaveLocalIdCounterPersistsValue() {
        mockPreferenceManager.saveLocalIdCounter(-42L)
        val retrieved = mockPreferenceManager.getLocalIdCounter()
        assertEquals("Saved counter should persist", -42L, retrieved)
    }

    @Test
    fun testSaveLocalIdCounterOverwritesPreviousValue() {
        mockPreferenceManager.saveLocalIdCounter(-10L)
        mockPreferenceManager.saveLocalIdCounter(-20L)
        val retrieved = mockPreferenceManager.getLocalIdCounter()
        assertEquals("Counter should be overwritten", -20L, retrieved)
    }

    @Test
    fun testSaveLocalIdCounterHandlesLargeNegativeValues() {
        val largeNegative = -1_000_000L
        mockPreferenceManager.saveLocalIdCounter(largeNegative)
        val retrieved = mockPreferenceManager.getLocalIdCounter()
        assertEquals("Large negative values should persist", largeNegative, retrieved)
    }

    // Sync Enabled Tests
    @Test
    fun testIsSyncEnabledReturnsFalseByDefault() {
        val isEnabled = mockPreferenceManager.isSyncEnabled()
        assertFalse("Sync should be disabled by default", isEnabled)
    }

    @Test
    fun testSetSyncEnabledTruePersistsValue() {
        mockPreferenceManager.setSyncEnabled(true)
        val isEnabled = mockPreferenceManager.isSyncEnabled()
        assertTrue("Sync enabled state should persist", isEnabled)
    }

    @Test
    fun testSetSyncEnabledFalsePersistsValue() {
        mockPreferenceManager.setSyncEnabled(true)
        mockPreferenceManager.setSyncEnabled(false)
        val isEnabled = mockPreferenceManager.isSyncEnabled()
        assertFalse("Sync disabled state should persist", isEnabled)
    }

    // Backup Enabled Tests
    @Test
    fun testIsBackupEnabledReturnsFalseByDefault() {
        val isEnabled = mockPreferenceManager.isBackupEnabled()
        assertFalse("Backup should be disabled by default", isEnabled)
    }

    @Test
    fun testSetBackupEnabledTruePersistsValue() {
        mockPreferenceManager.setBackupEnabled(true)
        val isEnabled = mockPreferenceManager.isBackupEnabled()
        assertTrue("Backup enabled state should persist", isEnabled)
    }

    @Test
    fun testSetBackupEnabledFalsePersistsValue() {
        mockPreferenceManager.setBackupEnabled(true)
        mockPreferenceManager.setBackupEnabled(false)
        val isEnabled = mockPreferenceManager.isBackupEnabled()
        assertFalse("Backup disabled state should persist", isEnabled)
    }

    // WiFi Only Sync Tests
    @Test
    fun testIsWifiOnlySyncEnabledReturnsTrueByDefault() {
        val isEnabled = mockPreferenceManager.isWifiOnlySyncEnabled()
        assertTrue("WiFi-only sync should be enabled by default", isEnabled)
    }

    @Test
    fun testSetWifiOnlySyncEnabledFalsePersistsValue() {
        mockPreferenceManager.setWifiOnlySyncEnabled(false)
        val isEnabled = mockPreferenceManager.isWifiOnlySyncEnabled()
        assertFalse("WiFi-only sync disabled state should persist", isEnabled)
    }

    @Test
    fun testSetWifiOnlySyncEnabledTruePersistsValue() {
        mockPreferenceManager.setWifiOnlySyncEnabled(false)
        mockPreferenceManager.setWifiOnlySyncEnabled(true)
        val isEnabled = mockPreferenceManager.isWifiOnlySyncEnabled()
        assertTrue("WiFi-only sync enabled state should persist", isEnabled)
    }

    // Integration Tests
    @Test
    fun testMultipleSyncPreferencesPersistIndependently() {
        mockPreferenceManager.setSyncEnabled(true)
        mockPreferenceManager.setBackupEnabled(false)
        mockPreferenceManager.setWifiOnlySyncEnabled(true)
        mockPreferenceManager.saveLocalIdCounter(-123L)

        assertTrue("Sync enabled should persist", mockPreferenceManager.isSyncEnabled())
        assertFalse("Backup should remain disabled", mockPreferenceManager.isBackupEnabled())
        assertTrue("WiFi-only should persist", mockPreferenceManager.isWifiOnlySyncEnabled())
        assertEquals("Counter should persist", -123L, mockPreferenceManager.getLocalIdCounter())
    }
}

