package com.my.mvistudymultimodule.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.my.mvistudymultimodule.core.model.MovieDetailModel

@Dao
interface MovieDetailDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovieDetail(movieDetail: MovieDetailModel): Long

    @Delete
    suspend fun deleteMovieDetail(movieDetail: MovieDetailModel): Int

    @Query("SELECT EXISTS(SELECT 1 FROM MovieDetailEntity WHERE id = :movieId)")
    suspend fun checkMovieDetail(movieId: Int): Boolean

//    @Query("SELECT * FROM saved_movies")
//    fun getAllSavedMovies() : Flow<List<MovieModelResult>>
//
//    @Query("SELECT * FROM saved_movies WHERE title LIKE '%' || :keyword || '%'")
//    fun getSearchSavedMovies(keyword : String) : LiveData<List<MovieModelResult>>
//
//    @Query("SELECT * FROM saved_movies WHERE title LIKE '%' || :keyword || '%'")
//    fun getSearchSavedMovies_using_stateflow(keyword : String) : Flow<List<MovieModelResult>>
}