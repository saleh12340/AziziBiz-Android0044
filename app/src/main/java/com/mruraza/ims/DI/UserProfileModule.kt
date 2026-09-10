package com.mruraza.ims.DI

import android.content.Context
import com.mruraza.ims.Data.Local.PrefsManager.ProfilePrefManager
import com.mruraza.ims.Data.RepoImpl.UserRepositoryImpl
import com.mruraza.ims.Domain.Repo.UserRepository
import com.mruraza.ims.Domain.UseCase.GetUserInfoUseCase
import com.mruraza.ims.Domain.UseCase.SaveUserInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProfileModule {
    @Provides
    @Singleton
    fun providePrefsManager(@ApplicationContext context: Context): ProfilePrefManager {
        return ProfilePrefManager(context)
    }

    @Provides
    @Singleton
    fun provideUserProfile(prefsManager: ProfilePrefManager): UserRepository {
        return UserRepositoryImpl(prefsManager)
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(repository: UserRepository): GetUserInfoUseCase {
        return GetUserInfoUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveUserInfoUseCase(repository: UserRepository): SaveUserInfoUseCase {
        return SaveUserInfoUseCase(repository)
    }
}