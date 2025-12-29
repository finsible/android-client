package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.data.local.EntityTypeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

@Entity
data class SyncMetadataEntity(
    @Id var localId: Long = 0,

    @Convert(converter = EntityTypeConverter::class, dbType = Int::class)
    @Unique
    var entityType: EntityType = EntityType.TRANSACTION,

    var period: String? = null,

    var lastSyncTime: Long = 0,
    var lastFullSyncTime: Long = 0
)