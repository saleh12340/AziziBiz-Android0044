package com.mruraza.ims.Domain.Model

data class Bill(
    val id : Long = 0L,
    val good: List<Pair<Goods,Int>>, // Pair of Goods and Quantity
    val customer: Customer,
    val date : String,
    val discount : Int = 0
)
