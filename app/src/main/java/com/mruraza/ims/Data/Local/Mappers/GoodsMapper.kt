package com.mruraza.ims.Data.Local.Mappers

import com.mruraza.ims.Data.Local.Enitites.GoodsEntity
import com.mruraza.ims.Domain.Model.Goods

object GoodsMapper {
    fun Goods.toEntity() = GoodsEntity(id,name,image,price,cp,description,quantity,supplier)
    fun GoodsEntity.toDomain() = Goods(id,name,image,price,cp,description,quantity,supplier)
}