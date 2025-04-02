package com.my.mvistudymultimodule.feature.compose.moviedetail.view

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.my.mvistudymultimodule.core.base.ComposeCustomScreen
import com.my.mvistudymultimodule.core.base.NavigationScreenName
import com.my.mvistudymultimodule.core.util.dpToSp
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.white
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.mainhome.view.ImageEmptyView
import com.my.mvistudymultimodule.feature.compose.moviedetail.event.ComposeMovieDetailScreenEvent
import com.my.mvistudymultimodule.feature.compose.moviedetail.event.ComposeMovieDetailViewModelEvent
import com.my.mvistudymultimodule.feature.compose.moviedetail.state.MovieDetailUiState
import com.my.mvistudymultimodule.feature.compose.moviedetail.viewmodel.ComposeMovieDetailViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@Composable
fun ComposeMovieDetailScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    composeMovieDetailViewModel: ComposeMovieDetailViewModel = hiltViewModel(),
    intent: Intent,
    movieId: Int
) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    val initExecute = rememberSaveable {
        mutableStateOf(true)
    }

    val movieDetailUiState = composeMovieDetailViewModel.movieDetailUiEvent.collectAsState().value

    ComposeMovieDetailUI(
        localContext = localContext,
        scope = scope,
        composeMovieDetailScreenEvent = {
            when(it) {
                is ComposeMovieDetailScreenEvent.OnBackClick -> {
                    if(navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    }
                }
            }
        },
        composeMovieDetailViewModelEvent = {
            when(it) {
                is ComposeMovieDetailViewModelEvent.GetMovieDetail -> {
                    composeMovieDetailViewModel.handleViewModelEvent(it)
                }
            }
        },
        movieDetailUiState = movieDetailUiState
    )

    LaunchedEffect(Unit) {
        if(initExecute.value) {
            initExecute.value = false
            composeMovieDetailViewModel.handleViewModelEvent(ComposeMovieDetailViewModelEvent.GetMovieDetail(movieId = movieId))
            delay(3000L)
            navController.navigate(route = NavigationScreenName.TestScreen.name)
        }
    }
}

@Composable
fun ComposeMovieDetailUI(
    localContext: Context,
    scope: CoroutineScope,
    composeMovieDetailScreenEvent: (ComposeMovieDetailScreenEvent) -> Unit,
    composeMovieDetailViewModelEvent: (ComposeMovieDetailViewModelEvent) -> Unit,
    movieDetailUiState: MovieDetailUiState
) {
    val rootScrollState = rememberScrollState()

    ComposeCustomScreen(
        backgroundColor = white()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeaderView(
                localContext = localContext,
                onBackClick = {
                    composeMovieDetailScreenEvent.invoke(ComposeMovieDetailScreenEvent.OnBackClick())
                },
                onSearchClick = {

                },
                title = movieDetailUiState.movieDetail?.title?:"-"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(state = rootScrollState),
            ) {
                GlideImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f) // 3:4 비율 유지
                        .clip(MaterialTheme.shapes.extraSmall),
                    imageModel = {
                        com.my.mvistudymultimodule.core.di.BuildConfig.BASE_MOVIE_POSTER + movieDetailUiState.movieDetail?.posterPath
                    },
                    previewPlaceholder = painterResource(id = com.my.mvistudymultimodule.core.base.R.drawable.ic_search_24_000000),
                    imageOptions = ImageOptions(
                        alignment = Alignment.TopCenter,
                        contentScale = ContentScale.FillHeight
                    ),
                    failure = {
                        ImageEmptyView()
                    }
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
                    text = movieDetailUiState.movieDetail?.title?:"-",
                    style = TextStyle(fontSize = dpToSp(dp = 20.dp), fontWeight = FontWeight.SemiBold),
                    lineHeight = 30.sp
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 5.dp),
                    text = movieDetailUiState.movieDetail?.overview?:"-",
                    style = TextStyle(fontSize = dpToSp(dp = 15.dp)),
                    lineHeight = 22.sp
                )
            }
        }
    }


    BackHandler {
        composeMovieDetailScreenEvent.invoke(ComposeMovieDetailScreenEvent.OnBackClick())
    }
}

@Composable
fun HeaderView(
    localContext: Context,
    onBackClick: () -> Unit,
    onSearchClick: () -> Unit,
    title: String
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
                painter = painterResource(com.my.mvistudymultimodule.core.base.R.drawable.ic_arrow_back_ios_new_24_000000),
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
                text = title
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
                painter = painterResource(com.my.mvistudymultimodule.core.base.R.drawable.ic_save_24_000000),
                contentDescription = "Search"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ComposeMovieDetailScreenPreview() {
    ComposeMovieDetailUI(
        localContext = LocalContext.current,
        scope = rememberCoroutineScope(),
        composeMovieDetailScreenEvent = {},
        composeMovieDetailViewModelEvent = {},
        movieDetailUiState = MovieDetailUiState()
    )
}