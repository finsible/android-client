package com.itsjeel01.finsiblefrontend.data.local.entity

import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.TransactionTypeConverter
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class CategoryEntity(
    @Id(assignable = true)
    var id: Long = 0,

    @Convert(converter = TransactionTypeConverter::class, dbType = Int::class)
    var type: TransactionType,

    var name: String,

    var color: String,
)
