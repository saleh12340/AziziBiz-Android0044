package com.mruraza.ims.Data.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mruraza.ims.Data.Local.Enitites.PurchaseBillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseBillDAO {
    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: PurchaseBillEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<PurchaseBillEntity>): List<Long>

    // READ
    @Query("SELECT * FROM PurchaseBillEntity WHERE id = :billId")
    suspend fun getBill(billId: Long): PurchaseBillEntity?

    @Query("SELECT * FROM PurchaseBillEntity")
    fun getAllBills(): Flow<List<PurchaseBillEntity>>

    // Last 1 month
    @Query("SELECT * FROM PurchaseBillEntity WHERE date BETWEEN :oneMonthAgo AND :today ORDER BY date ASC")
    fun getBillsInLastMonth(oneMonthAgo: String, today: String): Flow<List<PurchaseBillEntity>>

    // Last 1 year
    @Query("SELECT * FROM PurchaseBillEntity WHERE date BETWEEN :oneYearAgo AND :today ORDER BY date ASC")
    fun getBillsInLastYear(oneYearAgo: String, today: String): Flow<List<PurchaseBillEntity>>

    // Last 10 years
    @Query("SELECT * FROM PurchaseBillEntity WHERE date BETWEEN :tenYearsAgo AND :today ORDER BY date ASC")
    fun getBillsInLastTenYears(tenYearsAgo: String, today: String): Flow<List<PurchaseBillEntity>>

    // UPDATE
    @Update
    suspend fun updateBill(bill: PurchaseBillEntity)

    // DELETE
    @Delete
    suspend fun deleteBill(bill: PurchaseBillEntity)

    @Query("DELETE FROM PurchaseBillEntity WHERE id = :billId")
    suspend fun deleteBillById(billId: Long)

    @Query("DELETE FROM PurchaseBillEntity")
    suspend fun deleteAllBills()
}