package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.Status

interface SyncableEntity {
    var syncStatus: Status
    var lastSyncAttempt: Long?
    var syncError: String?
}

