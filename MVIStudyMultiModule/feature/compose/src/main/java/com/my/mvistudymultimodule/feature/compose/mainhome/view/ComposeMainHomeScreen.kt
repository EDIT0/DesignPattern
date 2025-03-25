package com.my.mvistudymultimodule.feature.compose.mainhome.view

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.my.mvistudymultimodule.core.base.ComposeCustomScreen
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.color7F000000
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.white
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.mainhome.event.ComposeMainHomeViewModelEvent
import com.my.mvistudymultimodule.feature.compose.mainhome.state.MovieListPagingUiState
import com.my.mvistudymultimodule.feature.compose.mainhome.viewmodel.ComposeMainHomeViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun ComposeMainHomeScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    composeMainHomeViewModel: ComposeMainHomeViewModel = hiltViewModel(),
    intent: Intent
) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    val movieListPagingUiState = composeMainHomeViewModel.movieListPagingUiState.collectAsState().value
    
    ComposeMainHomeUI(
        localContext = localContext,
        scope = scope,
        composeMainHomeViewModelEvent = {
            when(it) {
                is ComposeMainHomeViewModelEvent.GetPopularMovie -> {
                    composeMainHomeViewModel.handleViewModelEvent(it)
                }
            }
        },
        movieListPagingUiState = movieListPagingUiState,
        movieListPaging = movieListPagingUiState.movieList?.collectAsLazyPagingItems()
    )

    LaunchedEffect(Unit) {
        composeMainHomeViewModel.handleViewModelEvent(ComposeMainHomeViewModelEvent.GetPopularMovie())
    }
}

@Composable
fun ComposeMainHomeUI(
    localContext: Context,
    scope: CoroutineScope,
    composeMainHomeViewModelEvent: (ComposeMainHomeViewModelEvent) -> Unit,
    movieListPagingUiState: MovieListPagingUiState,
    movieListPaging: LazyPagingItems<MovieModel.MovieModelResult>?
) {

    // LoadingView 제어
    val isShowLoading = rememberSaveable {
        mutableStateOf(false)
    }

    ComposeCustomScreen(
        backgroundColor = white()
    ) {
        Column {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(movieListPaging?.itemCount ?: 0) { index ->
                    movieListPaging?.get(index)?.let { movie ->
                        Text(modifier = Modifier.padding(20.dp), text = "${movie.title}")
                    }
                }

                movieListPaging?.loadState?.let { loadStatus ->
                    LogUtil.i_dev("addLoadStateListener ${loadStatus}")

                    when(loadStatus.source.append) {
                        is LoadState.Error -> {
                            isShowLoading.value = false
                            item {
                                RetryView(
                                    retry = {
                                        movieListPaging.retry()
                                    },
                                    message = (loadStatus.source.append as LoadState.Error).error.localizedMessage?:""
                                )
                            }
                        }
                        is LoadState.Loading -> {
                            isShowLoading.value = true
                            item {
                                ListLoadingView()
                            }
                        }
                        is LoadState.NotLoading -> {
                            isShowLoading.value = false
                        }
                    }

                    when(loadStatus.source.prepend) {
                        is LoadState.Error -> {
                            item {
                                RetryView(
                                    retry = {
                                        movieListPaging.retry()
                                    },
                                    message = (loadStatus.source.prepend as LoadState.Error).error.localizedMessage?:""
                                )
                            }
                        }
                        is LoadState.Loading -> {

                        }
                        is LoadState.NotLoading -> {

                        }
                    }

                    when(loadStatus.source.refresh) {
                        is LoadState.Error -> {
                            item {
                                RetryView(
                                    retry = {
                                        movieListPaging.retry()
                                    },
                                    message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage?:""
                                )
                            }
                            isShowLoading.value = false
//
//                            if(xmlMainHomeVM.getMovieListPagingAdapter().itemCount == 0) {
//                                binding.tvMovieListEmpty.visibility = View.VISIBLE
//                                binding.rvMovie.visibility = View.GONE
//                            }
                        }
                        is LoadState.Loading -> {
                            isShowLoading.value = true
                            item {
                                ListLoadingView()
                            }
//                            binding.tvMovieListEmpty.visibility = View.GONE
//                            binding.rvMovie.visibility = View.VISIBLE
                        }
                        is LoadState.NotLoading -> {

                        }
                    }
                }
            }
        }

        LoadingView(isShow = isShowLoading.value)
    }
}

@Composable
fun LoadingView(
    isShow: Boolean
) {
    if(isShow) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun ListLoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun RetryView(
    retry: () -> Unit,
    message: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                retry.invoke()
            }
        ) {
            Text("다시 시도")
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeMainHomeScreenPreview() {
    ComposeMainHomeUI(
        localContext = LocalContext.current,
        scope = rememberCoroutineScope(),
        composeMainHomeViewModelEvent = {},
        movieListPagingUiState = MovieListPagingUiState(),
        movieListPaging = null
    )
}