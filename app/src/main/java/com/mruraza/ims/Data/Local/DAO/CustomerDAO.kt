package com.mruraza.ims.Data.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mruraza.ims.Data.Local.Enitites.CustomerEntity


@Dao
interface CustomerDAO {
    @Insert
    suspend fun insert(customer: CustomerEntity)

    @Update
    suspend fun update(customer: CustomerEntity)

    @Delete
    suspend fun delete(customer: CustomerEntity)

    @Query("SELECT * FROM CustomerEntity")
    fun getALLCustomers(): kotlinx.coroutines.flow.Flow<List<CustomerEntity>>
}