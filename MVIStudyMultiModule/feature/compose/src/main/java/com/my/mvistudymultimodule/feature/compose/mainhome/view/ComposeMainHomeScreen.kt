package com.my.mvistudymultimodule.feature.compose.mainhome.view

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.gson.Gson
import com.my.mvistudymultimodule.core.base.ComposeCustomScreen
import com.my.mvistudymultimodule.core.base.NavigationScreenName
import com.my.mvistudymultimodule.core.base.R
import com.my.mvistudymultimodule.core.di.BuildConfig
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.core.util.ToastUtil
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey300
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.white
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.mainhome.event.ComposeMainHomeScreenEvent
import com.my.mvistudymultimodule.feature.compose.mainhome.event.ComposeMainHomeViewModelEvent
import com.my.mvistudymultimodule.feature.compose.mainhome.state.MovieListPagingUiState
import com.my.mvistudymultimodule.feature.compose.mainhome.viewmodel.ComposeMainHomeViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ComposeMainHomeScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    composeMainHomeViewModel: ComposeMainHomeViewModel = hiltViewModel(),
    intent: Intent
) {
    val localView = LocalView.current
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    /**
     * 최초 실행 분기
     */
    val initExecute = rememberSaveable {
        mutableStateOf(true)
    }

    val movieListPagingUiState = composeMainHomeViewModel.movieListPagingUiState.collectAsState().value

    LaunchedEffect(Unit) {
        composeMainHomeViewModel.sideEffectEvent.collect {
            when(it) {
                is ComposeMainHomeViewModel.SideEffectEvent.ShowToast -> {
                    ToastUtil.makeToast(
                        view = localView,
                        message = it.message
                    )
                }
            }
        }
    }

    ComposeMainHomeUI(
        localContext = localContext,
        scope = scope,
        composeMainHomeScreenEvent = {
            when (it) {
                is ComposeMainHomeScreenEvent.OnBackClick -> {
                    if (localContext is ComponentActivity) {
                        localContext.finish()
                    }
                }

                is ComposeMainHomeScreenEvent.OnMovieClick -> {
                    LogUtil.d_dev("영화 클릭: ${it.movieInfo.title}")
                    val movieInfoStr = URLEncoder.encode(
                        Gson().toJson(it.movieInfo),
                        StandardCharsets.UTF_8.name()
                    )
                    navController.navigate(route = "${NavigationScreenName.ComposeMovieDetailScreen.name}/${it.movieInfo.id}/${movieInfoStr}")
                }

                is ComposeMainHomeScreenEvent.OnSearchClick -> {
                    LogUtil.d_dev("검색 클릭")
                    navController.navigate(route = "${NavigationScreenName.ComposeSearchMovieScreen.name}")
                }

                is ComposeMainHomeScreenEvent.OnSavedMovieFloatingButtonClick -> {
                    navController.navigate(route = "${NavigationScreenName.ComposeSavedMovieScreen.name}")
                }
            }
        },
        composeMainHomeViewModelEvent = {
            when (it) {
                is ComposeMainHomeViewModelEvent.GetPopularMovie -> {
                    composeMainHomeViewModel.handleViewModelEvent(it)
                }
            }
        },
        movieListPagingUiState = movieListPagingUiState,
        movieListPaging = movieListPagingUiState.movieList?.collectAsLazyPagingItems()
    )

    LaunchedEffect(Unit) {
        if (initExecute.value) {
            initExecute.value = false
            composeMainHomeViewModel.handleViewModelEvent(ComposeMainHomeViewModelEvent.GetPopularMovie())
        }
    }
}

@Composable
fun ComposeMainHomeUI(
    localContext: Context,
    scope: CoroutineScope,
    composeMainHomeScreenEvent: (ComposeMainHomeScreenEvent) -> Unit,
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
            HeaderView(
                localContext = localContext,
                onBackClick = {
                    composeMainHomeScreenEvent.invoke(ComposeMainHomeScreenEvent.OnBackClick())
                },
                onSearchClick = {
                    composeMainHomeScreenEvent.invoke(ComposeMainHomeScreenEvent.OnSearchClick())
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(movieListPaging?.itemCount ?: 0) { index ->
                    movieListPaging?.get(index)?.let { movie ->
                        MovieListItemView(
                            movieInfo = movie,
                            onClick = { item ->
                                composeMainHomeScreenEvent.invoke(
                                    ComposeMainHomeScreenEvent.OnMovieClick(
                                        movieInfo = item
                                    )
                                )
                            }
                        )
                    }
                }

                movieListPaging?.loadState?.let { loadStatus ->
                    LogUtil.i_dev("addLoadStateListener ${loadStatus}")

                    when (loadStatus.source.append) {
                        is LoadState.Error -> {
                            isShowLoading.value = false
                            item {
                                RetryView(
                                    localContext = localContext,
                                    retry = {
                                        movieListPaging.retry()
                                    },
                                    message = (loadStatus.source.append as LoadState.Error).error.localizedMessage
                                        ?: ""
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

                    when (loadStatus.source.prepend) {
                        is LoadState.Error -> {
                            item {
                                RetryView(
                                    localContext = localContext,
                                    retry = {
                                        movieListPaging.retry()
                                    },
                                    message = (loadStatus.source.prepend as LoadState.Error).error.localizedMessage
                                        ?: ""
                                )
                            }
                        }

                        is LoadState.Loading -> {

                        }

                        is LoadState.NotLoading -> {

                        }
                    }

                    when (loadStatus.source.refresh) {
                        is LoadState.Error -> {
                            item {
                                RetryView(
                                    localContext = localContext,
                                    retry = {
                                        movieListPaging.retry()
                                    },
                                    message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage
                                        ?: ""
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

        // 플로팅 버튼
        Box(
            modifier = Modifier
                .padding(end = 20.dp, bottom = 50.dp)
                .align(Alignment.BottomEnd)
                .clickable {
                    composeMainHomeScreenEvent.invoke(ComposeMainHomeScreenEvent.OnSavedMovieFloatingButtonClick())
                }
//            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .background(color = grey300(), shape = CircleShape)
                    .padding(15.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save_24_000000),
                    contentDescription = ""
                )
            }

        }

        // 스크린 전체 로딩뷰
        LoadingView(isShow = isShowLoading.value)

        // Compose masking
        Text(
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.BottomEnd),
            text = "Compose"
        )
    }

    BackHandler {
        composeMainHomeScreenEvent.invoke(ComposeMainHomeScreenEvent.OnBackClick())
    }
}

@Composable
fun HeaderView(
    localContext: Context,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val headerViewHeight = rememberSaveable {
        mutableIntStateOf(60)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerViewHeight.value.dp)
    ) {
        IconButton(
            modifier = Modifier
                .height(headerViewHeight.value.dp),
            onClick = {
                onBackClick.invoke()
            }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_arrow_back_ios_new_24_000000),
                contentDescription = "Back"
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .height(headerViewHeight.value.dp)
                .clickable {
                    onSearchClick.invoke()
                },
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 5.dp),
                text = localContext.getString(R.string.main_home_movie_search)
            )
        }

        IconButton(
            modifier = Modifier
                .height(headerViewHeight.value.dp),
            onClick = {
                onSearchClick.invoke()
            }
        ) {
            Image(
                painter = painterResource(R.drawable.ic_search_24_000000),
                contentDescription = "Search"
            )
        }
    }
}

@Composable
fun MovieListItemView(
    movieInfo: MovieModel.MovieModelResult,
    onClick: (item: MovieModel.MovieModelResult) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick.invoke(movieInfo)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.2f) // 화면 너비 1/5 사용
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f) // 3:4 비율 유지
                    .padding(5.dp)
                    .clip(MaterialTheme.shapes.extraSmall),
                imageModel = {
                    BuildConfig.BASE_MOVIE_POSTER + movieInfo.posterPath
                },
                previewPlaceholder = painterResource(id = R.drawable.ic_search_24_000000),
                imageOptions = ImageOptions(
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth
                ),
                failure = {
                    ImageEmptyView()
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = movieInfo.title.toString()
                )
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = movieInfo.releaseDate.toString()
                )
            }
        }
    }
}

@Composable
fun LoadingView(
    isShow: Boolean
) {
    if (isShow) {
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
    localContext: Context,
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
            Text(localContext.getString(R.string.common_retry))
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ImageEmptyView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(grey300(), RoundedCornerShape(3.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_save_24_000000),
            contentDescription = "ImageEmptyView"
        )
    }
}

@Preview
@Composable
private fun PreviewImageEmptyView() {
    ImageEmptyView()
}

@Preview(showBackground = true)
@Composable
fun ComposeMainHomeScreenPreview() {
    ComposeMainHomeUI(
        localContext = LocalContext.current,
        scope = rememberCoroutineScope(),
        composeMainHomeScreenEvent = {},
        composeMainHomeViewModelEvent = {},
        movieListPagingUiState = MovieListPagingUiState(),
        movieListPaging = null
    )
}