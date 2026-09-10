package com.mruraza.ims.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.ims.Domain.Model.Customer
import com.mruraza.ims.Domain.UseCase.CustomerUseCases
import com.mruraza.ims.Utils.Objects.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerManagementViewModel @Inject constructor(
    private val customerUseCases: CustomerUseCases
): ViewModel() {

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


    // all customer db operations
    val allCustomers = customerUseCases.getCustomers().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)

    fun addCustomer(customer: Customer){
        viewModelScope.launch { customerUseCases.addCustomer(customer) }
    }

    fun updateCustomer(customer: Customer){
        viewModelScope.launch { customerUseCases.updateCustomers(customer) }
    }

    fun deleteCustomer(customer: Customer){
        viewModelScope.launch { customerUseCases.deleteCustomers(customer) }
    }

}