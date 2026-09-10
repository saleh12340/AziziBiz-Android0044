package com.mruraza.ims.Data.RepoImpl

import com.mruraza.ims.Data.Local.DAO.PurchaseBillDAO
import com.mruraza.ims.Data.Local.Mappers.PurchaseBillMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.PurchaseBillMapper.toEntity
import com.mruraza.ims.Domain.Model.PurchaseBill
import com.mruraza.ims.Domain.Repo.PurchaseBillRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PurchaseBillRepoImpl @Inject constructor(
    private val purchaseBillDAO: PurchaseBillDAO
): PurchaseBillRepo {
    override suspend fun insert(bill: PurchaseBill) {
        purchaseBillDAO.insertBill(bill.toEntity())
    }

    override suspend fun insertAll(bills: List<PurchaseBill>) {
        purchaseBillDAO.insertBills(bills.map { it.toEntity() })
    }

    override suspend fun update(bill: PurchaseBill) {
        purchaseBillDAO.updateBill(bill.toEntity())
    }

    override suspend fun delete(bill: PurchaseBill) {
        purchaseBillDAO.deleteBill(bill.toEntity())
    }

    override suspend fun getAll(): Flow<List<PurchaseBill>> {
        return purchaseBillDAO.getAllBills().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteAll() {
        purchaseBillDAO.deleteAllBills()
    }

    override suspend fun deleteById(billId: Long) {
        purchaseBillDAO.deleteBillById(billId)
    }

    override suspend fun getPurchaseBillById(billId: Long): PurchaseBill? {
        return purchaseBillDAO.getBill(billId)?.toDomain()
    }

    override suspend fun getPurchaseBillsInLastMonth(
        oneMonthAgo: String,
        today: String
    ): Flow<List<PurchaseBill>> {
        return purchaseBillDAO.getBillsInLastMonth(oneMonthAgo, today).map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getPurchaseBillsInLastYear(
        oneYearAgo: String,
        today: String
    ): Flow<List<PurchaseBill>> {
        return purchaseBillDAO.getBillsInLastYear(oneYearAgo, today).map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getPurchaseBillsInLastTenYears(
        tenYearsAgo: String,
        today: String
    ): Flow<List<PurchaseBill>> {
        return purchaseBillDAO.getBillsInLastTenYears(tenYearsAgo, today).map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }

    }
}