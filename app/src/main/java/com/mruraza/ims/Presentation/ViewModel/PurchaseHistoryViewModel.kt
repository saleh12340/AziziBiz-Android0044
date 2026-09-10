package com.mruraza.ims.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.ims.Domain.UseCase.PurchaseBillUseCases
import com.mruraza.ims.Utils.Objects.Resources
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(
    private val purchaseBillUseCases: PurchaseBillUseCases
): ViewModel() {
    val allBills = purchaseBillUseCases.getAllBills().stateIn(viewModelScope, SharingStarted.Lazily, Resources.Loading)
}