package com.mruraza.ims.Data.RepoImpl

import com.mruraza.ims.Data.Local.PrefsManager.ProfilePrefManager
import com.mruraza.ims.Domain.Model.UserInfo
import com.mruraza.ims.Domain.Repo.UserRepository

class UserRepositoryImpl(private val prefsManager: ProfilePrefManager) : UserRepository {
    override fun getUserInfo(): UserInfo {
        return UserInfo(
            name = prefsManager.getData("name"),
            address = prefsManager.getData("address"),
            phone = prefsManager.getData("phone")
        )
    }

    override fun saveUserInfo(userInfo: UserInfo) {
        prefsManager.saveData("name", userInfo.name)
        prefsManager.saveData("address", userInfo.address)
        prefsManager.saveData("phone", userInfo.phone)
    }
}
