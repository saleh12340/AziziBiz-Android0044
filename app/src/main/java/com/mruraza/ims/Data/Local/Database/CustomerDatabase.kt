package com.mruraza.ims.Data.Local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mruraza.ims.Data.Local.DAO.CustomerDAO
import com.mruraza.ims.Data.Local.Enitites.CustomerEntity

@Database(entities = [CustomerEntity::class], version = 1)
abstract class CustomerDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDAO
}