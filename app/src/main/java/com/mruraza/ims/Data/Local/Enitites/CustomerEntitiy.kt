package com.mruraza.ims.Data.Local.Enitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L,
    val name: String,
    val phone: String ,
    val address: String ,
    val due: Double,
    val dueDate : String
)
