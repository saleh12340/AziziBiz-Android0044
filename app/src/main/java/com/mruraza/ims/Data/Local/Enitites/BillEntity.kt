package com.mruraza.ims.Data.Local.Enitites

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BillEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @Embedded(prefix = "customer_")
    val customer: CustomerEntity,

    val goodsJson: String ,
    val date: String,
    val discount: Int
)