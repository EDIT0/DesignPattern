package com.my.mvistudymultimodule.core.di

import android.content.Context
import androidx.room.Room
import com.my.mvistudymultimodule.core.database.MyMovieDatabase
import com.my.mvistudymultimodule.core.database.dao.MovieDetailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun getMovieDetailDao(myMovieDatabase: MyMovieDatabase): MovieDetailDao {
        return myMovieDatabase.movieDetailDao()
    }

    @Singleton
    @Provides
    fun getMyMovieDatabase(context: Context): MyMovieDatabase{
        return Room.databaseBuilder(
            context = context,
            klass = MyMovieDatabase::class.java,
            name = "MyMovieDatabase"
        ).fallbackToDestructiveMigration().build()
    }
}