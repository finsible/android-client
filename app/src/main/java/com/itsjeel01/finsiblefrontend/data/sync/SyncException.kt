package com.itsjeel01.finsiblefrontend.data.sync

/** Exception thrown during sync operations with retry information. */
class SyncException(
    message: String,
    val isRetryable: Boolean = true,
    cause: Throwable? = null
) : Exception(message, cause) {

    companion object {
        fun networkError(cause: Throwable) = SyncException(
            message = "Network error: ${cause.message}",
            isRetryable = true,
            cause = cause
        )

        fun serverError(code: Int, message: String) = SyncException(
            message = "Server error $code: $message",
            isRetryable = code >= 500  // 5xx errors are retryable
        )

        fun notFound() = SyncException(
            message = "Entity not found on server",
            isRetryable = false  // Don't retry 404s
        )

        fun unauthorized() = SyncException(
            message = "Authentication required",
            isRetryable = false  // Requires re-auth, not retry
        )

        fun conflict(details: String) = SyncException(
            message = "Conflict: $details",
            isRetryable = false  // Conflicts need resolution
        )
    }
}
