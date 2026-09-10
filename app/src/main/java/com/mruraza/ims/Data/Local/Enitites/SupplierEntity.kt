package com.mruraza.ims.Data.Local.Enitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val address: String,
    val contactInfo: String,
    val totalPaid: Double,
    val due: Double,
    val dueDate:String
)
