package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.EntityType
import com.itsjeel01.finsiblefrontend.data.local.EntityTypeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.Unique

/** Tracks sync metadata for different entity types and sync scopes. */
@Entity
data class SyncMetadataEntity(
    @Id var localId: Long = 0,

    @Unique var syncKey: String = "",

    @Convert(converter = EntityTypeConverter::class, dbType = Int::class)
    @Index var entityType: EntityType = EntityType.TRANSACTION,

    var lastSyncTime: Long = 0,
    var lastFullSyncTime: Long = 0
) {
    companion object {
        /** Build a sync key for an entity type with optional period. */
        fun buildSyncKey(entityType: EntityType, period: String? = null): String {
            return if (period != null) "${entityType.name}:$period" else entityType.name
        }

        /** Create a new SyncMetadataEntity for the given scope. */
        fun forScope(entityType: EntityType, period: String? = null): SyncMetadataEntity {
            return SyncMetadataEntity(
                syncKey = buildSyncKey(entityType, period),
                entityType = entityType
            )
        }
    }
}
