package com.mruraza.ims.Data.Local.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mruraza.ims.Data.Local.DAO.SupplierDAO
import com.mruraza.ims.Data.Local.Enitites.SupplierEntity


@Database(entities = [SupplierEntity::class], version = 1)
abstract class SupplierDatabase: RoomDatabase()  {
    abstract fun supplierDao(): SupplierDAO
}