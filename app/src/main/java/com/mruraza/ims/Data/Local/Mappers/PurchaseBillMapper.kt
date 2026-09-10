package com.mruraza.ims.Data.Local.Mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mruraza.ims.Data.Local.Enitites.PurchaseBillEntity
import com.mruraza.ims.Data.Local.Mappers.SupplierMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.SupplierMapper.toEntity
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.PurchaseBill

object PurchaseBillMapper {
    fun PurchaseBill.toEntity(): PurchaseBillEntity {
        val gson = Gson()
        return PurchaseBillEntity(
            id = id,
            supplier = supplier.toEntity(),
            goodsJson = gson.toJson(good),
            date = date,
            discount = discount
        )
    }

    fun PurchaseBillEntity.toDomain(): PurchaseBill {
        val gson = Gson()
        val type = object : TypeToken<List<Pair<Goods, Int>>>() {}.type
        return PurchaseBill(
            id = id,
            supplier = supplier.toDomain(),
            good = gson.fromJson(goodsJson, type),
            date = date,
            discount = discount
        )
    }
}