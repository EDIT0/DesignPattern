package com.example.mvvmarchitecturestudy.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieModelResult: MovieModelResult)

    @Query("SELECT * FROM saved_movies")
    fun getAllSavedMovies() : Flow<List<MovieModelResult>>

    @Query("SELECT * FROM saved_movies WHERE title LIKE '%' || :keyword || '%'")
    fun getSearchSavedMovies(keyword : String) : LiveData<List<MovieModelResult>>

    @Query("SELECT * FROM saved_movies WHERE title LIKE '%' || :keyword || '%'")
    fun getSearchSavedMovies_using_stateflow(keyword : String) : Flow<List<MovieModelResult>>

    @Delete
    suspend fun deleteSavedMovies(movieModelResult: MovieModelResult)

}