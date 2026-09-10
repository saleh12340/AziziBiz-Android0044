package com.mruraza.ims.Domain.Model

data class Customer(
    val id:Long = 0L,
    val name: String,
    val phone: String = "",
    val address: String = "",
    val due: Double=0.0,
    val dueDate:String = ""
)