package com.my.mvistudymultimodule.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.my.mvistudymultimodule.core.model.MovieDetailModel

@Dao
interface MovieDetailDao {
    @Insert
    suspend fun insertMovieDetail(movieDetail: MovieDetailModel): Long
}