package com.mruraza.ims.Data.Local.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mruraza.ims.Data.Local.Enitites.BillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDAO {
    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<BillEntity>): List<Long>

    // READ
    @Query("SELECT * FROM BillEntity WHERE id = :billId")
    suspend fun getBill(billId: Long): BillEntity?

    @Query("SELECT * FROM BillEntity")
    fun getAllBills(): Flow<List<BillEntity>>

    // Last 1 month
    @Query("SELECT * FROM BillEntity WHERE date BETWEEN :oneMonthAgo AND :today ORDER BY date ASC")
    fun getBillsInLastMonth(oneMonthAgo: String, today: String): Flow<List<BillEntity>>

    // Last 1 year
    @Query("SELECT * FROM BillEntity WHERE date BETWEEN :oneYearAgo AND :today ORDER BY date ASC")
    fun getBillsInLastYear(oneYearAgo: String, today: String): Flow<List<BillEntity>>

    // Last 10 years
    @Query("SELECT * FROM BillEntity WHERE date BETWEEN :tenYearsAgo AND :today ORDER BY date ASC")
    fun getBillsInLastTenYears(tenYearsAgo: String, today: String): Flow<List<BillEntity>>

    // UPDATE
    @Update
    suspend fun updateBill(bill: BillEntity)

    // DELETE
    @Delete
    suspend fun deleteBill(bill: BillEntity)

    @Query("DELETE FROM BillEntity WHERE id = :billId")
    suspend fun deleteBillById(billId: Long)

    @Query("DELETE FROM BillEntity")
    suspend fun deleteAllBills()
}