package com.my.mvistudymultimodule.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.my.mvistudymultimodule.core.database.dao.MovieDetailDao
import com.my.mvistudymultimodule.core.database.util.MovieDetailConverter
import com.my.mvistudymultimodule.core.model.MovieDetailModel

@Database(entities = [MovieDetailModel::class], version = 2, exportSchema = false)
@TypeConverters(MovieDetailConverter::class)
abstract class MyMovieDatabase: RoomDatabase() {
    abstract fun movieDetailDao(): MovieDetailDao
}