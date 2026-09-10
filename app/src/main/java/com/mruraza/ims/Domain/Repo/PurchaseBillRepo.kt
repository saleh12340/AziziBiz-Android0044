package com.mruraza.ims.Domain.Repo

import com.mruraza.ims.Domain.Model.PurchaseBill
import kotlinx.coroutines.flow.Flow

interface PurchaseBillRepo {
    suspend fun insert(bill: PurchaseBill)
    suspend fun insertAll(bills: List<PurchaseBill>)
    suspend fun update(bill: PurchaseBill)
    suspend fun delete(bill: PurchaseBill)
    suspend fun getAll(): Flow<List<PurchaseBill>>
    suspend fun deleteAll()
    suspend fun deleteById(billId: Long)
    suspend fun getPurchaseBillById(billId: Long): PurchaseBill?
    suspend fun getPurchaseBillsInLastMonth(
        oneMonthAgo: String,
        today: String
    ): Flow<List<PurchaseBill>>

    suspend fun getPurchaseBillsInLastYear(
        oneYearAgo: String,
        today: String
    ): Flow<List<PurchaseBill>>

    suspend fun getPurchaseBillsInLastTenYears(
        tenYearsAgo: String,
        today: String
    ): Flow<List<PurchaseBill>>
}