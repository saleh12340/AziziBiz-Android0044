package com.mruraza.ims.DI


import android.app.Application
import androidx.room.Room
import com.mruraza.ims.Data.Local.DAO.PurchaseBillDAO
import com.mruraza.ims.Data.Local.Database.PurchaseBillDatabase
import com.mruraza.ims.Data.RepoImpl.PurchaseBillRepoImpl
import com.mruraza.ims.Domain.Repo.PurchaseBillRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PurchaseBillDatabaseModule {
    @Provides
    @Singleton
    fun providesPurchaseBillDatabase(app: Application): PurchaseBillDatabase {
        return Room.databaseBuilder(
            app,
            PurchaseBillDatabase::class.java,
            "purchase_bill_db"
        ).build()
    }
    @Provides
    fun providesPurchaseBillDao(purchaseBillDatabase: PurchaseBillDatabase): PurchaseBillDAO = purchaseBillDatabase.purchaseBillDAO()
}


@Module
@InstallIn(SingletonComponent::class)
object PurchaseBillRepoModule {
    @Provides
    @Singleton
    fun providesPurchaseBillRepo(billDAO: PurchaseBillDAO): PurchaseBillRepo {
        return PurchaseBillRepoImpl(billDAO)
    }
}