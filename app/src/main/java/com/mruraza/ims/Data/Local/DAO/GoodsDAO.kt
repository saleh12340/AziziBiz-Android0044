package com.mruraza.ims.Data.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mruraza.ims.Data.Local.Enitites.GoodsEntity


@Dao
interface GoodsDAO {
    @Insert
    suspend fun insert(goodsEntity: GoodsEntity)

    @Update
    suspend fun update(goodsEntity: GoodsEntity)

    @Delete
    suspend fun delete(goodsEntity: GoodsEntity)

    @Query("SELECT * FROM GoodsEntity")
    fun getALLGoods(): kotlinx.coroutines.flow.Flow<List<GoodsEntity>>

//    @Query("SELECT * FROM GoodsEntity WHERE quantity>0 ")
//    fun getALLInStockGoods(): kotlinx.coroutines.flow.Flow<List<GoodsEntity>>
}