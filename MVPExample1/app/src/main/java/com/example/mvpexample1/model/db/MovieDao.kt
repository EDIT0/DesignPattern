package com.example.mvpexample1.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvpexample1.model.data.MovieModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    suspend fun insertMovie(movieModelResult: MovieModel.MovieModelResult)

    @Query("SELECT * FROM saved_movies")
    fun getAllSavedMovies() : Flow<List<MovieModel.MovieModelResult>>

    @Query("SELECT * FROM saved_movies WHERE title LIKE '%' || :keyword || '%'")
    fun getSearchSavedMovies(keyword : String) : LiveData<List<MovieModel.MovieModelResult>>

    @Delete
    suspend fun deleteSavedMovies(movieModelResult: MovieModel.MovieModelResult)

}