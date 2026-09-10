package com.mruraza.ims.Data.Local.Mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mruraza.ims.Data.Local.Enitites.BillEntity
import com.mruraza.ims.Data.Local.Mappers.CustomerMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.CustomerMapper.toEntity
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Model.Goods

object BillMapper {
    fun Bill.toEntity(): BillEntity {
        val gson = Gson()
        return BillEntity(
            id = id,
            customer = customer.toEntity(),
            goodsJson = gson.toJson(good),
            date = date,
            discount = discount
        )
    }

    fun BillEntity.toDomain(): Bill {
        val gson = Gson()
        val type = object : TypeToken<List<Pair<Goods, Int>>>() {}.type
        return Bill(
            id = id,
            customer = customer.toDomain(),
            good = gson.fromJson(goodsJson, type),
            date = date,
            discount = discount
        )
    }
}
