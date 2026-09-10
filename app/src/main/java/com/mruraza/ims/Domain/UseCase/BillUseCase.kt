package com.mruraza.ims.Domain.UseCase

import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Repo.BillRepo
import com.mruraza.ims.Utils.Objects.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddBill @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke(bill: Bill) = repository.insert(bill)
}

class AddAllBills @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke(bills: List<Bill>) = repository.insertAll(bills)
}

class updateBill @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke(bill: Bill) = repository.update(bill)
}

class deleteBill @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke(bill: Bill) = repository.delete(bill)
}

class getAllBills @Inject constructor(private val repository: BillRepo)  {
     operator fun invoke() : Flow<Resources<List<Bill>>> = flow {
         emit(Resources.Loading)
         repository.getAll()
             .collect { bills ->
                 emit(Resources.Success(bills))
             }
     }.flowOn(
         Dispatchers.IO
     )
}

class getBillsInLastMonth @Inject constructor(private val repository: BillRepo)  {
    operator fun invoke(oneMonthAgo: String, today: String) : Flow<Resources<List<Bill>>> = flow {
        emit(Resources.Loading)
        repository.getBillsInLastMonth(oneMonthAgo,today)
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}

class getBillsInLastYear @Inject constructor(private val repository: BillRepo)  {
    operator fun invoke(oneYearAgo: String, today: String) : Flow<Resources<List<Bill>>> = flow {
        emit(Resources.Loading)
        repository.getBillsInLastYear(oneYearAgo ,today)
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}

class getBillsInLastTenYears @Inject constructor(private val repository: BillRepo)  {
    operator fun invoke(tenYearsAgo: String, today: String) : Flow<Resources<List<Bill>>> = flow {
        emit(Resources.Loading)
        repository.getBillsInLastTenYears(tenYearsAgo,today)
            .collect { bills ->
                emit(Resources.Success(bills))
            }
    }.flowOn(
        Dispatchers.IO
    )
}


class getBillById @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke(billId: Long) = repository.getBillById(billId)
}

class deleteAllBills @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke() = repository.deleteAll()
}

class deleteBillById @Inject constructor(private val repository: BillRepo)  {
    suspend operator fun invoke(billId: Long) = repository.deleteById(billId)
}

data class BillUseCases @Inject constructor(
    val addBill: AddBill,
    val addAllBills: AddAllBills,
    val updateBill: updateBill,
    val deleteBill: deleteBill,
    val getAllBills: getAllBills,
    val getBillsInLastMonth: getBillsInLastMonth,
    val getBillsInLastYear: getBillsInLastYear,
    val getBillsInLastTenYears: getBillsInLastTenYears,
    val getBillById: getBillById,
    val deleteAllBills: deleteAllBills,
    val deleteBillById: deleteBillById
)
