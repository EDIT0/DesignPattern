package com.my.mvistudymultimodule.data.repository.local.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.my.mvistudymultimodule.core.database.dao.MovieDetailDao
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.Random

private const val STARTING_PAGE_INDEX = 1

//class GetSavedMoviePagingSource(
//    private val movieDetailDao: MovieDetailDao
//): PagingSource<Int, MovieDetailModel>() {
//
//    private val pageLimit = 10
//
//    // 데이터가 새로고침되거나 첫 로드 후 무효화되었을 때 키를 반환하여 load()로 전달
//    override fun getRefreshKey(state: PagingState<Int, MovieDetailModel>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val closestPage = state.closestPageToPosition(anchorPosition)
//            closestPage?.prevKey?.plus(1)?: closestPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetailModel> {
//        // LoadParams : 로드할 키와 항목 수 , LoadResult : 로드 작업의 결과
//        return try {
//            delay(1000L)
//
//            // 키 값이 없을 경우 기본값을 사용함
//            val position = params.key ?: STARTING_PAGE_INDEX
//
//            val totalPages = movieDetailDao.getSavedMovieCount() / pageLimit + 1
//
//            Log.i("MYTAG", "${position} / ${totalPages}")
//            // 데이터를 제공하는 인스턴스의 메소드 사용
//            val offset = (position - 1) * pageLimit
//            Log.i("MYTAG", "offset: ${offset}")
//            val response = movieDetailDao.getSavedMoviePaging(pageLimit, offset)
//            Log.i("MYTAG", "아이템 갯수: ${movieDetailDao.getSavedMovieCount()}")
//
//            // 에러 발생 시키기
//            val randomNumber = Random().nextInt(10) + 1
//            if(randomNumber > 8) {
//                throw IOException("IOException Error 만듬")
//            }
//
//            response?.let {
//                LoadResult.Page(
//                    data = response,
//                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
//                    nextKey = if (position == totalPages) null else position + 1,
//                    itemsBefore = 0,
//                    itemsAfter = 0
//                )
//            }?: throw Exception("PagingSource Error")
//
//        } catch (e: IOException) {
//            LoadResult.Error(e)
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//}