package com.mruraza.ims.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.ims.Domain.Model.Bill
import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.PurchaseBill
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Domain.UseCase.BillUseCases
import com.mruraza.ims.Domain.UseCase.CustomerUseCases
import com.mruraza.ims.Domain.UseCase.GoodsUseCases
import com.mruraza.ims.Domain.UseCase.PurchaseBillUseCases
import com.mruraza.ims.Domain.UseCase.SupplierUseCases
import com.mruraza.ims.Utils.Objects.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseBillViewModel @Inject constructor(
    private val purchaseBillUseCases: PurchaseBillUseCases,
    private val goodsUseCases: GoodsUseCases,
    private val customerUseCases: CustomerUseCases,
    private val supplierUseCases: SupplierUseCases
) : ViewModel() {

    private val _discount = MutableStateFlow<Int>(0)
    val discount: StateFlow<Int> = _discount
    val updateDiscount: (Int) -> Unit = { discount ->
        _discount.value = discount
    }

    private val _selectedSupplier = MutableStateFlow<Supplier?>(null)
    val selectedSupplier: StateFlow<Supplier?> = _selectedSupplier
    val updateSupplier: (Supplier) -> Unit = { supplier ->
        _selectedSupplier.value = supplier
    }

    private val _bill = MutableStateFlow<List<Pair<Goods, Int>>>(listOf())
    val bill: StateFlow<List<Pair<Goods, Int>>> = _bill
    val updateBill: (List<Pair<Goods, Int>>) -> Unit = { bill ->
        _bill.value = bill
    }

    // lets get the suppliers
    val allSuppliers =
        supplierUseCases.getSuppliers().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    fun addSupplier(supplier: Supplier) {
        viewModelScope.launch {
            supplierUseCases.addSuppliers(supplier)
        }
    }



    // for the goods
    val allGoods =
        goodsUseCases.getGoods().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    fun addGoods(goods: Goods) {
        viewModelScope.launch {
            goodsUseCases.addGoods(goods)
        }
    }


    // lets get the customers
    val allCustomers =
        customerUseCases.getCustomers().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    fun addCustomer(customer: Customer) {
        viewModelScope.launch {
            customerUseCases.addCustomer(customer)
        }
    }


    // all the usecases

    val allPurchaseBills =
        purchaseBillUseCases.getAllBills().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)


    fun addPurchaseBill(bill: PurchaseBill) {
        viewModelScope.launch {
            purchaseBillUseCases.addBill(bill)
        }
    }

    fun addAllBills(bills: List<PurchaseBill>) {
        viewModelScope.launch {
            purchaseBillUseCases.addAllBills(bills)
        }
    }

    fun deleteBill(bill: PurchaseBill) {
        viewModelScope.launch {
            purchaseBillUseCases.deleteBill(bill)
        }
    }

    fun updateBill(bill: PurchaseBill) {
        viewModelScope.launch {
            purchaseBillUseCases.updateBill(bill)
        }
    }

    fun deleteAllBills() {
        viewModelScope.launch {
            purchaseBillUseCases.deleteAllBills()
        }
    }

    fun deleteBillById(billId: Long) {
        viewModelScope.launch {
            purchaseBillUseCases.deleteBillById(billId)
        }
    }

    fun getBillById(billId: Long) {
        viewModelScope.launch {
            purchaseBillUseCases.getBillById(billId)
        }

    }
}