package com.mruraza.ims.DI

import android.app.Application
import androidx.room.Room
import com.mruraza.ims.Data.Local.DAO.GoodsDAO
import com.mruraza.ims.Data.Local.Database.GoodsDatabase
import com.mruraza.ims.Data.RepoImpl.GoodsRepoImpl
import com.mruraza.ims.Domain.Repo.GoodsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GoodsDatabaseModule {
    @Provides
    @Singleton
    fun provideGoodsDatabase(app: Application): GoodsDatabase {
        return Room.databaseBuilder(
            app,
            GoodsDatabase::class.java,
            "goods_db"
        ).build()
    }

    @Provides
    fun providesGoodsDao(goodsDatabase: GoodsDatabase): GoodsDAO = goodsDatabase.goodsDao()
}


@Module
@InstallIn(SingletonComponent::class)
object GoodsRepositoryModule {
    @Provides
    @Singleton
    fun provideGoodsRepository(goodsDao: GoodsDAO): GoodsRepo {
        return GoodsRepoImpl(goodsDao)
    }

}