package com.mruraza.ims.Domain.Model

data class Goods(
    val id: Long = 0L,
    val name: String,
    val image: String = "android.resource://com.mruraza.ims/drawable/product_image_not_available",
    val price: Double,
    val cp : Double,
    val description: String = "Description Not Added",
    val quantity: Int = 0,
    val supplier: String = "Supplier Not Added"
)
