package com.mruraza.ims.Data.RepoImpl

import com.mruraza.ims.Data.Local.DAO.GoodsDAO
import com.mruraza.ims.Data.Local.Mappers.GoodsMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.GoodsMapper.toEntity
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Repo.GoodsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoodsRepoImpl @Inject constructor(private val dao: GoodsDAO): GoodsRepo {
    override suspend fun insert(goods: Goods) {
        dao.insert(goods.toEntity())
    }

    override suspend fun update(goods: Goods) {
        dao.update(goods.toEntity())
    }

    override suspend fun delete(goods: Goods) {
        dao.delete(goods.toEntity())
    }

    override suspend fun getAll(): Flow<List<Goods>> {
        return dao.getALLGoods().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

}