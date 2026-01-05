package com.itsjeel01.finsiblefrontend.data.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/** Unit tests for SyncException factory methods and retry logic. */
class SyncExceptionTest {

    @Test
    fun testNetworkErrorIsRetryable() {
        val cause = IOException("Connection timeout")
        val exception = SyncException.networkError(cause)

        assertTrue("Network errors should be retryable", exception.isRetryable)
        assertTrue("Message should contain cause", exception.message!!.contains("Connection timeout"))
        assertEquals("Cause should be preserved", cause, exception.cause)
    }

    @Test
    fun testServerError5xxIsRetryable() {
        val exception = SyncException.serverError(503, "Service Unavailable")

        assertTrue("5xx errors should be retryable", exception.isRetryable)
        assertTrue("Message should contain code", exception.message!!.contains("503"))
        assertTrue("Message should contain message", exception.message!!.contains("Service Unavailable"))
    }

    @Test
    fun testServerError4xxIsNotRetryable() {
        val exception = SyncException.serverError(400, "Bad Request")

        assertFalse("4xx errors should not be retryable", exception.isRetryable)
        assertTrue("Message should contain code", exception.message!!.contains("400"))
    }

    @Test
    fun testNotFoundIsNotRetryable() {
        val exception = SyncException.notFound()

        assertFalse("404 errors should not be retryable", exception.isRetryable)
        assertTrue("Message should indicate not found", exception.message!!.contains("not found"))
    }

    @Test
    fun testUnauthorizedIsNotRetryable() {
        val exception = SyncException.unauthorized()

        assertFalse("401 errors should not be retryable", exception.isRetryable)
        assertTrue("Message should indicate auth required", exception.message!!.contains("Authentication"))
    }

    @Test
    fun testConflictIsNotRetryable() {
        val exception = SyncException.conflict("Version mismatch")

        assertFalse("Conflicts should not be retryable", exception.isRetryable)
        assertTrue("Message should contain details", exception.message!!.contains("Version mismatch"))
    }

    @Test
    fun testCustomExceptionWithCause() {
        val cause = IllegalStateException("Invalid state")
        val exception = SyncException("Custom error", isRetryable = true, cause = cause)

        assertTrue("Should be retryable as specified", exception.isRetryable)
        assertEquals("Message should match", "Custom error", exception.message)
        assertEquals("Cause should be preserved", cause, exception.cause)
    }

    @Test
    fun testCustomExceptionDefaultRetryable() {
        val exception = SyncException("Default retry behavior")

        assertTrue("Should be retryable by default", exception.isRetryable)
        assertNull("Should have no cause", exception.cause)
    }

    @Test
    fun testServerErrorBoundary499IsNotRetryable() {
        val exception = SyncException.serverError(499, "Client closed request")
        assertFalse("499 should not be retryable", exception.isRetryable)
    }

    @Test
    fun testServerErrorBoundary500IsRetryable() {
        val exception = SyncException.serverError(500, "Internal Server Error")
        assertTrue("500 should be retryable", exception.isRetryable)
    }

    @Test
    fun testServerErrorBoundary599IsRetryable() {
        val exception = SyncException.serverError(599, "Network timeout error")
        assertTrue("599 should be retryable", exception.isRetryable)
    }

    @Test
    fun testServerErrorBoundary600IsRetryable() {
        val exception = SyncException.serverError(600, "Custom error")
        assertTrue("600 is >= 500 so should be retryable", exception.isRetryable)
    }
}

