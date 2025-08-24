package com.my.mvistudymultimodule.feature.compose.searchmovie.view

import android.content.Context
import android.content.Intent
import android.view.View
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.my.mvistudymultimodule.core.util.dpToSp
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey300
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey500
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.white
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.searchmovie.event.ComposeSearchMovieScreenEvent
import com.my.mvistudymultimodule.feature.compose.searchmovie.event.ComposeSearchMovieViewModelEvent
import com.my.mvistudymultimodule.feature.compose.searchmovie.state.SearchUiState
import com.my.mvistudymultimodule.feature.compose.searchmovie.viewmodel.ComposeSearchMovieViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ComposeSearchMovieScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    composeSearchMovieViewModel: ComposeSearchMovieViewModel = hiltViewModel(),
    intent: Intent
) {

    val localView = LocalView.current
    val localContext = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    /**
     * 최초 실행 분기
     */
    val initExecute = rememberSaveable {
        mutableStateOf(true)
    }

    val sideEffectEvent = composeSearchMovieViewModel.sideEffectEvent
    val searchUiState = composeSearchMovieViewModel.searchStateUiState.collectAsState().value
    val searchUiStatePaging = searchUiState.searchMovieList?.collectAsLazyPagingItems()

    ComposeSearchMovieScreenUI(
        localView = localView,
        localContext = localContext,
        focusManager = focusManager,
        scope = scope,
        initExecute = initExecute,
        composeSearchMovieScreenEvent = {
            when(it) {
                is ComposeSearchMovieScreenEvent.OnBackClick -> {
                    navController.popBackStack()
                }
                is ComposeSearchMovieScreenEvent.OnMovieClick -> {
                    LogUtil.d_dev("영화 클릭: ${it.movieInfo.title}")
                    val movieInfoStr = URLEncoder.encode(
                        Gson().toJson(it.movieInfo),
                        StandardCharsets.UTF_8.name()
                    )
                    navController.navigate(route = "${NavigationScreenName.ComposeMovieDetailScreen.name}/${it.movieInfo.id}/${movieInfoStr}")
                }
            }
        },
        composeSearchMovieViewModelEvent = {
            when(it) {
                is ComposeSearchMovieViewModelEvent.GetSearchMovie -> {
                    composeSearchMovieViewModel.handleViewModelEvent(it)
                }
                is ComposeSearchMovieViewModelEvent.SaveCurrentSearchKeyword -> {
                    composeSearchMovieViewModel.handleViewModelEvent(it)
                }
            }
        },
        sideEffectEvent = sideEffectEvent,
        searchUiState = searchUiState,
        searchUiStatePaging = searchUiStatePaging
    )

    LaunchedEffect(Unit) {
        if (initExecute.value) {
            initExecute.value = false
        }
    }
}

@Composable
fun ComposeSearchMovieScreenUI(
    localView: View,
    localContext: Context,
    focusManager: FocusManager,
    scope: CoroutineScope,
    initExecute: MutableState<Boolean>,
    composeSearchMovieScreenEvent: (ComposeSearchMovieScreenEvent) -> Unit,
    composeSearchMovieViewModelEvent: (ComposeSearchMovieViewModelEvent) -> Unit,
    sideEffectEvent: Flow<ComposeSearchMovieViewModel.SideEffectEvent>,
    searchUiState: SearchUiState,
    searchUiStatePaging: LazyPagingItems<MovieModel.MovieModelResult>?
) {

    // LoadingView 제어
    val isShowLoading = rememberSaveable {
        mutableStateOf(false)
    }

    val listState = rememberLazyListState()
    
    ComposeCustomScreen(
        backgroundColor = white()
    ) {
        Column {
            HeaderView(
                localContext = localContext,
                onBackClick = {
                    composeSearchMovieScreenEvent.invoke(ComposeSearchMovieScreenEvent.OnBackClick())
                },
                onSearchClick = {
                    composeSearchMovieViewModelEvent.invoke(ComposeSearchMovieViewModelEvent.GetSearchMovie())
                },
                searchUiState = searchUiState,
                composeSearchMovieViewModelEvent = composeSearchMovieViewModelEvent,
                initExecute = initExecute
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                state = listState
            ) {
                items(
                    count = searchUiStatePaging?.itemCount ?: 0,
                    key = { index -> // Movie ID 기준으로 데이터를 비교하여 필요한 부분만 업데이트
                        searchUiStatePaging?.get(index)?.id ?: index
                    }
                ) { index ->
                    searchUiStatePaging?.get(index)?.let { movie ->
                        MovieListItemView(
                            movieInfo = movie,
                            onClick = { item ->
                                composeSearchMovieScreenEvent.invoke(
                                    ComposeSearchMovieScreenEvent.OnMovieClick(
                                        movieInfo = item
                                    )
                                )
                            }
                        )
                    }
                }

                // 데이터가 없을 경우
                if(searchUiStatePaging?.itemCount == 0) {
                    item {
                        PagingDataEmptyView(
                            localContext = localContext,
                            searchKeyword = searchUiState.searchKeyword?:"",
                            isShowLoading = isShowLoading
                        )
                    }
                }

                searchUiStatePaging?.loadState?.let { loadStatus ->
                    LogUtil.i_dev("addLoadStateListener ${loadStatus}")

                    when (loadStatus.source.append) {
                        is LoadState.Error -> {
                            isShowLoading.value = false
                            item {
                                RetryView(
                                    localContext = localContext,
                                    retry = {
                                        searchUiStatePaging.retry()
                                    },
                                    message = (loadStatus.source.append as LoadState.Error).error.localizedMessage ?: ""
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
                                        searchUiStatePaging.retry()
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
                                        searchUiStatePaging.retry()
                                    },
                                    message = (loadStatus.source.refresh as LoadState.Error).error.localizedMessage
                                        ?: ""
                                )
                            }
                            isShowLoading.value = false
                        }

                        is LoadState.Loading -> {
                            isShowLoading.value = true
                            item {
                                ListLoadingView()
                            }
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

    // 현재 스크롤 위치
    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            LogUtil.d_dev("현재 인덱스: $index, 스크롤 위치 오프셋: $offset")
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(Unit) {
        sideEffectEvent.collect {
            when(it) {
                is ComposeSearchMovieViewModel.SideEffectEvent.Toast -> {
                    ToastUtil.makeToast(localView, it.message)
                }
            }
        }
    }

    BackHandler {
        composeSearchMovieScreenEvent.invoke(ComposeSearchMovieScreenEvent.OnBackClick())
    }
}

@Composable
fun HeaderView(
    localContext: Context,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    composeSearchMovieViewModelEvent: (ComposeSearchMovieViewModelEvent) -> Unit,
    searchUiState: SearchUiState,
    initExecute: MutableState<Boolean>
) {
    val headerViewHeight = rememberSaveable {
        mutableIntStateOf(60)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val isTextBoxFocused = rememberSaveable { mutableStateOf(false) }

    // 키보드 올리기
    LaunchedEffect(Unit) {
        if(initExecute.value) {
            delay(100L)
            focusRequester.requestFocus()
            keyboardController?.show()
            LogUtil.d_dev("MYTAG 키보드 올리기")
        }
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

        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .height(headerViewHeight.value.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isTextBoxFocused.value = focusState.isFocused
                },
            value = searchUiState.searchKeyword?:"",
            onValueChange = {
                composeSearchMovieViewModelEvent.invoke(ComposeSearchMovieViewModelEvent.SaveCurrentSearchKeyword(searchKeyword = it, isDirectSearch = false))
            },
            singleLine = true,
            textStyle = TextStyle(fontSize = dpToSp(dp = 20.dp)),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(isTextBoxFocused.value) {
                        innerTextField()
                    } else if(searchUiState.searchKeyword.isNullOrBlank()) {
                        Text(
                            text = localContext.getString(R.string.search_movie_hint),
                            color = grey500()
                        )
                    } else {
                        innerTextField()
                    }
                }
            }
        )

        if(searchUiState.searchKeyword?.isNotEmpty() == true) {
            IconButton(
                modifier = Modifier
                    .height(headerViewHeight.value.dp),
                onClick = {
                    composeSearchMovieViewModelEvent.invoke(ComposeSearchMovieViewModelEvent.SaveCurrentSearchKeyword(searchKeyword = "", isDirectSearch = false))
                }
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_cancel_24_000000),
                    contentDescription = "Search"
                )
            }
        }

        IconButton(
            modifier = Modifier
                .height(headerViewHeight.value.dp),
            onClick = {
                composeSearchMovieViewModelEvent.invoke(ComposeSearchMovieViewModelEvent.SaveCurrentSearchKeyword(searchKeyword = searchUiState.searchKeyword?:"", isDirectSearch = true))
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

@Composable
fun PagingDataEmptyView(
    localContext: Context,
    searchKeyword: String,
    isShowLoading: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(white()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(searchKeyword.isEmpty()) { // 검색어가 없을 경우
            Text(
                modifier = Modifier
                    .padding(vertical = 30.dp),
                text = localContext.getString(R.string.search_movie_list_state_no_keyword)
            )
        } else if(isShowLoading.value) { // 로딩 중일 경우
            Text(
                modifier = Modifier
                    .padding(vertical = 30.dp),
                text = localContext.getString(R.string.search_movie_list_state_loading)
            )
        } else {
            Text(
                modifier = Modifier
                    .padding(vertical = 30.dp),
                text = localContext.getString(R.string.search_movie_list_state_no_result, searchKeyword)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagingDataEmptyViewPreview() {
    PagingDataEmptyView(
        localContext = LocalContext.current,
        searchKeyword = "검색어",
        isShowLoading = rememberSaveable { mutableStateOf(false) },
    )
}

@Preview(showBackground = true)
@Composable
fun ComposeSearchMovieScreenPreview() {
    ComposeSearchMovieScreenUI(
        localView = LocalView.current,
        localContext = LocalContext.current,
        focusManager = LocalFocusManager.current,
        scope = rememberCoroutineScope(),
        initExecute = rememberSaveable { mutableStateOf(true) },
        composeSearchMovieScreenEvent = {},
        composeSearchMovieViewModelEvent = {},
        searchUiState = SearchUiState(),
        searchUiStatePaging = null,
        sideEffectEvent = flow {  }
    )
}