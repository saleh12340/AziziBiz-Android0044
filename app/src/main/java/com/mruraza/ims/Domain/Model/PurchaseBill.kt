package com.mruraza.ims.Domain.Model

data class PurchaseBill(
    val id: Long=0L,
    val good: List<Pair<Goods,Int>>, // Pair of Goods and Quantity
    val supplier: Supplier,
    val date : String,
    val discount : Int = 0
)
