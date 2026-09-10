package com.mruraza.ims.Data.RepoImpl

import com.mruraza.ims.Data.Local.DAO.BillDAO
import com.mruraza.ims.Data.Local.Enitites.BillEntity
import com.mruraza.ims.Data.Local.Mappers.BillMapper.toDomain
import com.mruraza.ims.Data.Local.Mappers.BillMapper.toEntity
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Repo.BillRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BillRepoImpl @Inject constructor(private val billDAO: BillDAO) : BillRepo {
    override suspend fun insert(bill: Bill) {
        billDAO.insertBill(bill.toEntity())
    }

    override suspend fun insertAll(bills: List<Bill>) {
        val billsToEntity: List<BillEntity> = bills.map { it -> it.toEntity() }
        billDAO.insertBills(billsToEntity)
    }

    override suspend fun update(bill: Bill) {
        billDAO.updateBill(bill.toEntity())
    }

    override suspend fun delete(bill: Bill) {
        billDAO.deleteBill(bill.toEntity())
    }

    override suspend fun getAll(): Flow<List<Bill>> {
        return billDAO.getAllBills().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun deleteAll() {
        billDAO.deleteAllBills()
    }

    override suspend fun deleteById(billId: Long) {
        billDAO.deleteBillById(billId)
    }

    override suspend fun getBillById(billId: Long): Bill? {
        return billDAO.getBill(billId)?.toDomain()
    }

    override suspend fun getBillsInLastMonth(
        oneMonthAgo: String,
        today: String
    ): Flow<List<Bill>> {
        return billDAO.getBillsInLastMonth(oneMonthAgo,today).map { entities->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getBillsInLastYear(
        oneYearAgo: String,
        today: String
    ): Flow<List<Bill>> {
        return billDAO.getBillsInLastYear(oneYearAgo,today).map { entities->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun getBillsInLastTenYears(
        tenYearsAgo: String,
        today: String
    ): Flow<List<Bill>> {
        return billDAO.getBillsInLastTenYears(tenYearsAgo,today).map { entities->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }
}