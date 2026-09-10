package com.mruraza.ims.DI

import android.app.Application
import androidx.room.Room
import com.mruraza.ims.Data.Local.DAO.BillDAO
import com.mruraza.ims.Data.Local.Database.BillDatabase
import com.mruraza.ims.Data.RepoImpl.BillRepoImpl
import com.mruraza.ims.Domain.Repo.BillRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillMDatabaseModule {
    @Provides
    @Singleton
    fun providesBillDatabase(app: Application): BillDatabase{
        return Room.databaseBuilder(
            app,
            BillDatabase::class.java,
            "bill_db"
        ).build()
    }

    @Provides
    fun providesBillDao(billDatabase: BillDatabase): BillDAO = billDatabase.billDAO()
}


@Module
@InstallIn(SingletonComponent::class)
object BillRepoModule {
    @Provides
    @Singleton
    fun providesBillRepo(billDAO: BillDAO): BillRepo {
        return BillRepoImpl(billDAO)
    }
}