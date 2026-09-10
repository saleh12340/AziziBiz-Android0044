package com.mruraza.ims.Domain.UseCase

import com.mruraza.ims.Domain.Model.UserInfo
import com.mruraza.ims.Domain.Repo.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(): UserInfo = repository.getUserInfo()
}

class SaveUserInfoUseCase @Inject constructor(private val repository: UserRepository) {
    operator fun invoke(userInfo: UserInfo) = repository.saveUserInfo(userInfo)
}