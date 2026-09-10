package com.mruraza.ims.DI

import android.app.Application
import androidx.room.Room
import com.mruraza.ims.Data.Local.DAO.CustomerDAO
import com.mruraza.ims.Data.Local.Database.CustomerDatabase
import com.mruraza.ims.Data.RepoImpl.CustomerRepoImpl
import com.mruraza.ims.Domain.Repo.CustomerRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CustomerDatabaseModule{

    @Provides
    @Singleton
    fun providesCustomerDatabase(app: Application): CustomerDatabase{
        return Room.databaseBuilder(
            app,
            CustomerDatabase::class.java,
            "customer_db"
        ).build()
    }


    @Provides
    fun providesCustomerDao(customerDatabase: CustomerDatabase) : CustomerDAO= customerDatabase.customerDao()
}

@Module
@InstallIn(SingletonComponent::class)
object CustomerRepoModule{
    @Provides
    @Singleton
    fun providesCustomerRepo(customerDAO: CustomerDAO): CustomerRepo {
        return CustomerRepoImpl(customerDAO)
    }
}