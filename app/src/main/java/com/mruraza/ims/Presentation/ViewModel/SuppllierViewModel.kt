package com.mruraza.ims.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.ims.Domain.Model.Goods
import com.mruraza.ims.Domain.Model.Supplier
import com.mruraza.ims.Domain.UseCase.GoodsUseCases
import com.mruraza.ims.Domain.UseCase.SupplierUseCases
import com.mruraza.ims.Utils.Objects.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SuppliersViewModel @Inject constructor(
    private val supplierUseCases: SupplierUseCases
) : ViewModel() {
    // search
    private val _searchedText = MutableStateFlow("")
    val searchedText = _searchedText
    val updateSearchedText: (String) -> Unit = {
        _searchedText.value = it
    }
    private val _isSearchButtonClicked = MutableStateFlow(false)
    val isSearchButtonClicked = _isSearchButtonClicked
    val updateIsSearchButtonClicked: () -> Unit = {
        isSearchButtonClicked.value = !isSearchButtonClicked.value
    }

    // morevert
    private val _isMoreVertButtonClicked = MutableStateFlow(false)
    val isMoreVertButtonClicked = _isMoreVertButtonClicked
    val updateIsMoreVertButtonClicked: () -> Unit = {
        isMoreVertButtonClicked.value = !isMoreVertButtonClicked.value
    }

    // all operations of the suppliers
    // for the Supplier

    val allSupplier =
        supplierUseCases.getSuppliers().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    fun addSupplier(supplier: Supplier) {
        viewModelScope.launch { supplierUseCases.addSuppliers(supplier) }
    }

    fun updateSuppliers(supplier: Supplier){
        viewModelScope.launch { supplierUseCases.updateSuppliers(supplier) }
    }

    fun deleteSuppliers(supplier: Supplier){
        viewModelScope.launch { supplierUseCases.deleteSuppliers(supplier) }
    }

}

