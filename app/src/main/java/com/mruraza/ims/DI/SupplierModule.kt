package com.mruraza.ims.DI

import android.app.Application
import androidx.room.Room
import com.mruraza.ims.Data.Local.DAO.GoodsDAO
import com.mruraza.ims.Data.Local.DAO.SupplierDAO
import com.mruraza.ims.Data.Local.Database.GoodsDatabase
import com.mruraza.ims.Data.Local.Database.SupplierDatabase
import com.mruraza.ims.Data.RepoImpl.GoodsRepoImpl
import com.mruraza.ims.Data.RepoImpl.SupplierRepoImpl
import com.mruraza.ims.Domain.Repo.GoodsRepo
import com.mruraza.ims.Domain.Repo.SupplierRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupplierDatabaseModule {
    @Provides
    @Singleton
    fun provideSupplierDatabase(app: Application): SupplierDatabase {
        return Room.databaseBuilder(
            app,
            SupplierDatabase::class.java,
            "suppliers_db"
        ).build()
    }

    @Provides
    fun providesSupplierDao(supplierDatabase: SupplierDatabase): SupplierDAO = supplierDatabase.supplierDao()
}


@Module
@InstallIn(SingletonComponent::class)
object SupplierRepositoryModule {
    @Provides
    @Singleton
    fun provideSupplierRepository(supplierDao: SupplierDAO): SupplierRepo {
        return SupplierRepoImpl(supplierDao)
    }

}