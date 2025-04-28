package com.my.mvistudymultimodule.feature.compose.savedmovie.view

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey300
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.white
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.savedmovie.event.ComposeSavedMovieHomeScreenEvent
import com.my.mvistudymultimodule.feature.compose.savedmovie.event.ComposeSavedMovieViewModelEvent
import com.my.mvistudymultimodule.feature.compose.savedmovie.viewmodel.ComposeSavedMovieViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ComposeSavedMovieScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    composeSavedMovieViewModel: ComposeSavedMovieViewModel = hiltViewModel(),
    intent: Intent
) {

    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    /**
     * 최초 실행 분기
     */
    val initExecute = rememberSaveable {
        mutableStateOf(true)
    }

    val savedMovieListPagingUiState = composeSavedMovieViewModel.savedMovieListPagingUiState.collectAsState().value
    val savedMoviePaging = savedMovieListPagingUiState.savedMovieList?.collectAsLazyPagingItems()

    ComposeSavedMovieUI(
        localContext = localContext,
        scope = scope,
        composeSavedMovieHomeScreenEvent = {
            when(it) {
                is ComposeSavedMovieHomeScreenEvent.OnBackClick -> {
                    navController.popBackStack()
                }
                is ComposeSavedMovieHomeScreenEvent.OnMovieClick -> {
                    LogUtil.d_dev("영화 클릭: ${it.movieInfo.title}")
                    val movieInfoStr = URLEncoder.encode(
                        Gson().toJson(it.movieInfo),
                        StandardCharsets.UTF_8.name()
                    )
                    navController.navigate(route = "${NavigationScreenName.ComposeMovieDetailScreen.name}/${it.movieInfo.id}/${movieInfoStr}")
                }
            }
        },
        savedMovieListPaging = savedMoviePaging
    )

    LaunchedEffect(Unit) {
        if (initExecute.value) {
            initExecute.value = false
            composeSavedMovieViewModel.handleViewModelEvent(ComposeSavedMovieViewModelEvent.GetSavedMovie())
        }
    }
}

@Composable
fun ComposeSavedMovieUI(
    localContext: Context,
    scope: CoroutineScope,
    composeSavedMovieHomeScreenEvent: (ComposeSavedMovieHomeScreenEvent) -> Unit,
    savedMovieListPaging: LazyPagingItems<MovieDetailModel>?
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
                    composeSavedMovieHomeScreenEvent.invoke(ComposeSavedMovieHomeScreenEvent.OnBackClick())
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    count = savedMovieListPaging?.itemCount ?: 0,
                    key = { index -> // Movie ID 기준으로 데이터를 비교하여 필요한 부분만 업데이트
                        savedMovieListPaging?.get(index)?.id ?: index
                    }
                ) { index ->
                    savedMovieListPaging?.get(index)?.let { movie ->
                        MovieListItemView(
                            movieInfo = movie,
                            onClick = { item ->
                                composeSavedMovieHomeScreenEvent.invoke(
                                    ComposeSavedMovieHomeScreenEvent.OnMovieClick(
                                        movieInfo = item
                                    )
                                )
                            }
                        )
                    }
                }

                savedMovieListPaging?.loadState?.let { loadStatus ->
                    LogUtil.i_dev("addLoadStateListener ${loadStatus}")

                    when (loadStatus.source.append) {
                        is LoadState.Error -> {
                            isShowLoading.value = false
                            item {
                                RetryView(
                                    localContext = localContext,
                                    retry = {
                                        savedMovieListPaging.retry()
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
                                        savedMovieListPaging.retry()
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
                                        savedMovieListPaging.retry()
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
        composeSavedMovieHomeScreenEvent.invoke(ComposeSavedMovieHomeScreenEvent.OnBackClick())
    }
}

@Composable
fun HeaderView(
    localContext: Context,
    onBackClick: () -> Unit
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
                .height(headerViewHeight.value.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 5.dp),
                text = localContext.getString(R.string.saved_movie_title)
            )
        }
    }
}

@Composable
fun MovieListItemView(
    movieInfo: MovieDetailModel,
    onClick: (item: MovieDetailModel) -> Unit
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
                    text = movieInfo.originalTitle.toString()
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
fun ComposeSavedMovieScreenPreview() {
    ComposeSavedMovieUI(
        localContext = LocalContext.current,
        scope = rememberCoroutineScope(),
        composeSavedMovieHomeScreenEvent = {},
        savedMovieListPaging = null,
    )
}