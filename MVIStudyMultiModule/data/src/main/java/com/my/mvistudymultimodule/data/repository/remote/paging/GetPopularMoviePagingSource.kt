package com.my.mvistudymultimodule.data.repository.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.data.BuildConfig
import com.my.mvistudymultimodule.data.api.ApiService
import kotlinx.coroutines.delay
import java.io.IOException
import java.util.Random

private const val STARTING_PAGE_INDEX = 1

class GetPopularMoviePagingSource(
    private val apiService: ApiService,
    private val language: String
): PagingSource<Int, MovieModel.MovieModelResult>() {

    // 데이터가 새로고침되거나 첫 로드 후 무효화되었을 때 키를 반환하여 load()로 전달
    override fun getRefreshKey(state: PagingState<Int, MovieModel.MovieModelResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1)?: closestPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieModel.MovieModelResult> {
        // LoadParams : 로드할 키와 항목 수 , LoadResult : 로드 작업의 결과
        return try {
            delay(1000L)

            // 키 값이 없을 경우 기본값을 사용함
            val position = params.key ?: STARTING_PAGE_INDEX

            // 데이터를 제공하는 인스턴스의 메소드 사용
            val response = apiService.getPopularMovie(BuildConfig.API_KEY, language, position)

            var post: MovieModel? = null
            post = response.body()

            // 에러 발생 시키기
            val randomNumber = Random().nextInt(10) + 1
            if(randomNumber > 5) {
                throw IOException("IOException Error 만듬")
            }

            post?.let {
                LoadResult.Page(
                    data = post.movieModelResults,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (position == post.totalPages) null else position + 1,
                    itemsBefore = 0,
                    itemsAfter = 0
                )
            }?: throw Exception("PagingSource Error")

        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}