package com.itsjeel01.finsiblefrontend.data.sync

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit tests for ScopeManager lifecycle management. */
@OptIn(ExperimentalCoroutinesApi::class)
class ScopeManagerTest {

    private lateinit var scopeManager: ScopeManager

    @Before
    fun setUp() {
        scopeManager = ScopeManager()
    }

    @Test
    fun `scope is initially active`() {
        assertTrue("Scope should be initially active", scopeManager.scope.isActive)
    }

    @Test
    fun `reset cancels ongoing operations and creates new scope`() = runTest {
        val originalScope = scopeManager.scope
        var jobCompleted = false

        // Launch a job in the original scope
        val job = originalScope.launch {
            try {
                delay(10000) // Long delay to ensure it gets cancelled
                jobCompleted = true
            } catch (e: Exception) {
                // Job was cancelled
            }
        }

        // Reset the scope
        scopeManager.reset()

        // Wait a bit to ensure cancellation propagates
        delay(100)

        // Verify the job was cancelled
        assertFalse("Job should be cancelled after reset", job.isActive)
        assertFalse("Job should not have completed", jobCompleted)

        // Verify new scope is different from original
        val newScope = scopeManager.scope
        assertNotSame("New scope should be different from original", originalScope, newScope)
        assertTrue("New scope should be active", newScope.isActive)
    }

    @Test
    fun `shutdown cancels all operations`() = runTest {
        var jobCompleted = false

        // Launch a job in the scope
        val job = scopeManager.scope.launch {
            try {
                delay(10000) // Long delay to ensure it gets cancelled
                jobCompleted = true
            } catch (e: Exception) {
                // Job was cancelled
            }
        }

        // Shutdown the scope
        scopeManager.shutdown()

        // Wait a bit to ensure cancellation propagates
        delay(100)

        // Verify the job was cancelled
        assertFalse("Job should be cancelled after shutdown", job.isActive)
        assertFalse("Job should not have completed", jobCompleted)
    }

    @Test
    fun `reset allows launching new jobs in new scope`() = runTest {
        // Reset to create a new scope
        scopeManager.reset()

        var jobCompleted = false

        // Launch a job in the new scope
        val job = scopeManager.scope.launch {
            jobCompleted = true
        }

        // Wait for job to complete
        job.join()

        // Verify the job completed successfully
        assertTrue("Job should complete in new scope", jobCompleted)
        assertFalse("Job should not be active after completion", job.isActive)
    }

    @Test
    fun `multiple resets work correctly`() = runTest {
        val scopes = mutableListOf<kotlinx.coroutines.CoroutineScope>()
        scopes.add(scopeManager.scope)

        // Reset multiple times
        repeat(3) {
            scopeManager.reset()
            scopes.add(scopeManager.scope)
        }

        // Verify all scopes are different
        for (i in 0 until scopes.size - 1) {
            for (j in i + 1 until scopes.size) {
                assertNotSame(
                    "Each reset should create a different scope",
                    scopes[i],
                    scopes[j]
                )
            }
        }

        // Verify the latest scope is active
        assertTrue("Latest scope should be active", scopeManager.scope.isActive)
    }

    @Test
    fun `jobs in old scope are cancelled after reset`() = runTest {
        val jobs = mutableListOf<Job>()

        // Launch multiple jobs
        repeat(3) { index ->
            jobs.add(scopeManager.scope.launch {
                delay(10000)
            })
        }

        // Reset the scope
        scopeManager.reset()

        // Wait a bit for cancellation
        delay(100)

        // Verify all jobs were cancelled
        jobs.forEach { job ->
            assertFalse("Job should be cancelled after reset", job.isActive)
        }
    }
}
