package com.mruraza.ims.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.UseCase.BillUseCases
import com.mruraza.ims.Domain.UseCase.CustomerUseCases
import com.mruraza.ims.Domain.UseCase.GoodsUseCases
import com.mruraza.ims.Domain.UseCase.SupplierUseCases
import com.mruraza.ims.Utils.Objects.DateHelper
import com.mruraza.ims.Utils.Objects.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val billUseCases: BillUseCases,
    private val customerUseCases: CustomerUseCases,
    private val suppliersUseCases: SupplierUseCases,
    private val goodsUseCases: GoodsUseCases
) : ViewModel() {
    val allBills =
        billUseCases.getAllBills().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    private val today = DateHelper.todayDate()


    private val oneMonthAgo =
        DateHelper.dateToString(DateHelper.stringToDate(today)?.minusMonths(1)!!)
    val getBillsInLastMonth: StateFlow<Resources<List<Bill>>> =
        billUseCases.getBillsInLastMonth(oneMonthAgo, today)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    private val oneYearAgo =
        DateHelper.dateToString(DateHelper.stringToDate(today)?.minusYears(1)!!)
    val getBillsInLastYear: StateFlow<Resources<List<Bill>>> =
        billUseCases.getBillsInLastYear(oneYearAgo, today)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    private val tenYearsAgo =
        DateHelper.dateToString(DateHelper.stringToDate(today)?.minusYears(10)!!)
    val getBillsInLastTenYears: StateFlow<Resources<List<Bill>>> =
        billUseCases.getBillsInLastTenYears(tenYearsAgo, today)
            .stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)


    val allCustomers = customerUseCases.getCustomers().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)
    val allSuppliers = suppliersUseCases.getSuppliers().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    val allGoods = goodsUseCases.getGoods().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

}