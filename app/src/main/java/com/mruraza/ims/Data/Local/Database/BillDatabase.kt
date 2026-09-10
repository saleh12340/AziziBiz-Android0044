package com.mruraza.ims.Data.Local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mruraza.ims.Data.Local.Converters.GoodsListConverter
import com.mruraza.ims.Data.Local.DAO.BillDAO
import com.mruraza.ims.Data.Local.Enitites.BillEntity

@Database(entities = [BillEntity::class], version = 1)
@TypeConverters(GoodsListConverter::class)
abstract class BillDatabase : RoomDatabase() {
    abstract fun billDAO() : BillDAO
}