package com.mruraza.ims.Data.Local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mruraza.ims.Data.Local.DAO.GoodsDAO
import com.mruraza.ims.Data.Local.Enitites.GoodsEntity


@Database(entities = [GoodsEntity::class], version = 1)
abstract class GoodsDatabase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDAO
}