package com.mruraza.ims.Domain.Model

data class Supplier(
    val id: Long = 0L,
    val name: String,
    val address: String = "",
    val contactInfo: String = "",
    val totalPaid : Double = 0.0,
    val due : Double = 0.0,
    val dueDate:String=""
)
