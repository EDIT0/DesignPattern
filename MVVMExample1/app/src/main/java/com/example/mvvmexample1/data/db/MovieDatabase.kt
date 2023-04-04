package com.example.mvvmexample1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvmexample1.data.model.MovieModel

@Database(
    entities = [MovieModel.MovieModelResult::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDAO() : MovieDao
}