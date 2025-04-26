package com.itsjeel01.finsiblefrontend.core.storage.objectbox.entity

import com.itsjeel01.finsiblefrontend.data.model.TransactionType
import com.itsjeel01.finsiblefrontend.data.model.TransactionTypeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CategoryEntity(
    @Id(assignable = true) var id: Long = 0,
    @Convert(converter = TransactionTypeConverter::class, dbType = Int::class)
    var type: TransactionType,
    var name: String,
    var color: Int,
)