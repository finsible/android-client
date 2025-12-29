package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.common.OperationType
import com.itsjeel01.finsiblefrontend.common.Status
import com.itsjeel01.finsiblefrontend.data.local.EntityTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.OperationTypeConverter
import com.itsjeel01.finsiblefrontend.data.local.StatusConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
data class PendingOperationEntity(
    @Id var localId: Long = 0,

    @Convert(converter = EntityTypeConverter::class, dbType = Int::class)
    @Index var entityType: EntityType? = null,

    @Convert(converter = OperationTypeConverter::class, dbType = Int::class)
    var operationType: OperationType? = null,

    var entityId: Long = 0,                // Server ID (0 for CREATE until synced)
    var localEntityId: Long = 0,           // Local temp ID for CREATE operations
    var payload: String = "",              // JSON serialized request body
    @Index var createdAt: Long = 0,
    var retryCount: Int = 0,
    var lastError: String? = null,

    @Convert(converter = StatusConverter::class, dbType = Int::class)
    @Index var status: Status = Status.PENDING
)