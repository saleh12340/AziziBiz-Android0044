package com.mruraza.ims.Domain.UseCase

import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Model.PurchaseBill
import com.mruraza.ims.Domain.Repo.BillRepo
import com.mruraza.ims.Domain.Repo.PurchaseBillRepo
import com.mruraza.ims.Utils.Objects.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddPurchaseBill @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke(bill: PurchaseBill) = repository.insert(bill)
}

class AddAllPurchaseBills @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke(bills: List<PurchaseBill>) = repository.insertAll(bills)
}

class updatePurchaseBill @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke(bill: PurchaseBill) = repository.update(bill)
}

class deletePurchaseBill @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke(bill: PurchaseBill) = repository.delete(bill)
}

class getAllPurchaseBills @Inject constructor(private val repository: PurchaseBillRepo)  {
    operator fun invoke() : Flow<Resources<List<PurchaseBill>>> = flow {
        emit(Resources.Loading)
        repository.getAll()
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}

class getPurchaseBillsInLastMonth @Inject constructor(private val repository: PurchaseBillRepo)  {
    operator fun invoke(oneMonthAgo: String, today: String) : Flow<Resources<List<PurchaseBill>>> = flow {
        emit(Resources.Loading)
        repository.getPurchaseBillsInLastMonth(oneMonthAgo,today)
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}

class getPurchaseBillsInLastYear @Inject constructor(private val repository: PurchaseBillRepo)  {
    operator fun invoke(oneYearAgo: String, today: String) : Flow<Resources<List<PurchaseBill>>> = flow {
        emit(Resources.Loading)
        repository.getPurchaseBillsInLastYear(oneYearAgo ,today)
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}

class getPurchaseBillsInLastTenYears @Inject constructor(private val repository: PurchaseBillRepo)  {
    operator fun invoke(tenYearsAgo: String, today: String) : Flow<Resources<List<PurchaseBill>>> = flow {
        emit(Resources.Loading)
        repository.getPurchaseBillsInLastTenYears(tenYearsAgo,today)
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}


class getPurchaseBillById @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke(billId: Long) = repository.getPurchaseBillById(billId)
}

class deleteAllPurchaseBills @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke() = repository.deleteAll()
}

class deletePurchaseBillById @Inject constructor(private val repository: PurchaseBillRepo)  {
    suspend operator fun invoke(billId: Long) = repository.deleteById(billId)
}

data class PurchaseBillUseCases @Inject constructor(
    val addBill: AddPurchaseBill,
    val addAllBills: AddAllPurchaseBills,
    val updateBill: updatePurchaseBill,
    val deleteBill: deletePurchaseBill,
    val getAllBills: getAllPurchaseBills,
    val getBillsInLastMonth: getPurchaseBillsInLastMonth,
    val getBillsInLastYear: getPurchaseBillsInLastYear,
    val getBillsInLastTenYears: getPurchaseBillsInLastTenYears,
    val getBillById: getPurchaseBillById,
    val deleteAllBills: deleteAllPurchaseBills,
    val deleteBillById: deletePurchaseBillById
)
