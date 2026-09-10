package com.mruraza.ims.Domain.Repo

import com.mruraza.ims.Domain.Model.UserInfo

interface UserRepository {
    fun getUserInfo(): UserInfo
    fun saveUserInfo(userInfo: UserInfo)
}