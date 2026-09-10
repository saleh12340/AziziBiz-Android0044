package com.mruraza.ims.Domain.Repo

import com.mruraza.ims.Domain.Model.Goods
import kotlinx.coroutines.flow.Flow

interface GoodsRepo {
    suspend fun insert(goods: Goods)
    suspend fun update(goods: Goods)
    suspend fun delete(goods: Goods)
    suspend fun getAll(): Flow<List<Goods>>
}