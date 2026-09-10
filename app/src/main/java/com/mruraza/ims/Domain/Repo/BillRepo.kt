package com.mruraza.ims.Domain.Repo

import com.mruraza.ims.Domain.Model.Bill
import kotlinx.coroutines.flow.Flow

interface BillRepo {
    suspend fun insert(bill: Bill)
    suspend fun insertAll(bills: List<Bill>)
    suspend fun update(bill: Bill)
    suspend fun delete(bill: Bill)
    suspend fun getAll(): Flow<List<Bill>>
    suspend fun deleteAll()
    suspend fun deleteById(billId: Long)
    suspend fun getBillById(billId: Long): Bill?
    suspend fun getBillsInLastMonth(oneMonthAgo: String, today: String): Flow<List<Bill>>
    suspend fun getBillsInLastYear(oneYearAgo: String, today: String): Flow<List<Bill>>
    suspend fun getBillsInLastTenYears(tenYearsAgo: String, today: String): Flow<List<Bill>>
}