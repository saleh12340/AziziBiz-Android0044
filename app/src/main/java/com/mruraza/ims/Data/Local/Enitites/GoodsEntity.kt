package com.mruraza.ims.Data.Local.Enitites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GoodsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val image: String ,
    val price: Double,
    val cp : Double,
    val description: String,
    val quantity: Int ,
    val supplier: String
)
