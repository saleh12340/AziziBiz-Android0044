package com.mruraza.ims.Presentation.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mruraza.ims.Domain.Model.UserInfo
import com.mruraza.ims.Domain.UseCase.GetUserInfoUseCase
import com.mruraza.ims.Domain.UseCase.SaveUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val saveUserInfoUseCase: SaveUserInfoUseCase
) : ViewModel() {

    private val defaultBusiness = UserInfo(
        name = "بقالة العزي للمواد الغذائية",
        address = "",
        phone = "776425052"
    )

    private val _userInfo = MutableStateFlow(defaultBusiness)
    val userInfo: StateFlow<UserInfo> = _userInfo

    private val _isEditing = MutableStateFlow(
        mapOf("name" to false, "address" to false, "phone" to false)
    )
    val isEditing: StateFlow<Map<String, Boolean>> = _isEditing

    init { loadUserInfo() }

    fun refresh() { loadUserInfo() }

    private fun loadUserInfo() {
        val saved = getUserInfoUseCase()
        if (saved.name.isBlank() && saved.phone.isBlank()) {
            _userInfo.value = defaultBusiness
            viewModelScope.launch { saveUserInfoUseCase(defaultBusiness) }
        } else {
            _userInfo.value = saved
        }
    }

    fun toggleEditing(field: String) {
        _isEditing.value = _isEditing.value.toMutableMap().apply {
            this[field] = !(this[field] ?: false)
        }
    }

    fun updateUserInfo(newUserInfo: UserInfo) {
        viewModelScope.launch {
            saveUserInfoUseCase(newUserInfo)
            _userInfo.value = newUserInfo
        }
    }
}