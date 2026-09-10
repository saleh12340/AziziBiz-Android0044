package com.mruraza.ims.Data.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mruraza.ims.Data.Local.Enitites.SupplierEntity

@Dao
interface SupplierDAO {
    @Insert
    suspend fun insert(supplier: SupplierEntity)

    @Update
    suspend fun update(supplier: SupplierEntity)

    @Delete
    suspend fun delete(supplier: SupplierEntity)

    @Query("SELECT * FROM SupplierEntity")
    fun getAllSuppliers(): kotlinx.coroutines.flow.Flow<List<SupplierEntity>>

}