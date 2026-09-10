package com.mruraza.ims.Data.Local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mruraza.ims.Data.Local.Converters.GoodsListConverter
import com.mruraza.ims.Data.Local.DAO.PurchaseBillDAO
import com.mruraza.ims.Data.Local.Enitites.PurchaseBillEntity


@Database(entities = [PurchaseBillEntity::class], version = 1)
@TypeConverters(GoodsListConverter::class)
abstract class PurchaseBillDatabase : RoomDatabase(){
    abstract fun purchaseBillDAO(): PurchaseBillDAO
}