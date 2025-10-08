package com.my.mvistudymultimodule.feature.compose.moviedetail.view

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.LazyPagingItems
import com.my.mvistudymultimodule.core.base.ComposeCustomScreen
import com.my.mvistudymultimodule.core.base.R
import com.my.mvistudymultimodule.core.di.BuildConfig
import com.my.mvistudymultimodule.core.model.MovieDetailModel
import com.my.mvistudymultimodule.core.model.MovieModel
import com.my.mvistudymultimodule.core.model.MovieReviewModel
import com.my.mvistudymultimodule.core.util.ToastUtil
import com.my.mvistudymultimodule.core.util.dpToSp
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.deepPurple500
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey300
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey50
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey500
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey600
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey800
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.grey900
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.white
import com.my.mvistudymultimodule.feature.compose.home.view.ui.theme.yellowA400
import com.my.mvistudymultimodule.feature.compose.home.viewmodel.ComposeHomeViewModel
import com.my.mvistudymultimodule.feature.compose.mainhome.view.ImageEmptyView
import com.my.mvistudymultimodule.feature.compose.moviedetail.event.ComposeMovieDetailScreenEvent
import com.my.mvistudymultimodule.feature.compose.moviedetail.event.ComposeMovieDetailViewModelEvent
import com.my.mvistudymultimodule.feature.compose.moviedetail.state.MovieDetailUiState
import com.my.mvistudymultimodule.feature.compose.moviedetail.viewmodel.ComposeMovieDetailViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

@Composable
fun ComposeMovieDetailScreen(
    navController: NavController,
    composeHomeViewModel: ComposeHomeViewModel,
    composeMovieDetailViewModel: ComposeMovieDetailViewModel = hiltViewModel(),
    intent: Intent,
    movieId: Int,
    movieInfo: MovieModel.MovieModelResult
) {
    val localView = LocalView.current
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    val initExecute = rememberSaveable {
        mutableStateOf(true)
    }

    val movieDetailUiState = composeMovieDetailViewModel.movieDetailUiState.collectAsState().value
    val movieReviewListPagingUiState = composeMovieDetailViewModel.movieReviewListPagingUiState.collectAsState().value

    LaunchedEffect(Unit) {
        composeMovieDetailViewModel.sideEffectEvent.collectLatest { event ->
            when (event) {
                is ComposeMovieDetailViewModel.SideEffectEvent.ShowToast -> {
                    ToastUtil.makeToast(localView, event.message)
                }
            }
        }
    }

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
                is ComposeMovieDetailViewModelEvent.SaveMovieDetail -> {
                    composeMovieDetailViewModel.handleViewModelEvent(it)
                }
                is ComposeMovieDetailViewModelEvent.DeleteMovieDetail -> {
                    composeMovieDetailViewModel.handleViewModelEvent(it)
                }
                is ComposeMovieDetailViewModelEvent.GetMovieReview -> {
                    composeMovieDetailViewModel.handleViewModelEvent( it)
                }
            }
        },
        movieDetailUiState = movieDetailUiState,
        movieReviewListPaging = movieReviewListPagingUiState.reviewList?.collectAsLazyPagingItems()
    )

    LaunchedEffect(Unit) {
        if(initExecute.value) {
            initExecute.value = false
            composeMovieDetailViewModel.handleViewModelEvent(ComposeMovieDetailViewModelEvent.GetMovieDetail(movieId = movieId, movieInfo = movieInfo))
            composeMovieDetailViewModel.handleViewModelEvent(ComposeMovieDetailViewModelEvent.GetMovieReview(movieId = movieId))
//            delay(3000L)
//            navController.navigate(route = NavigationScreenName.TestScreen.name)
        }
    }

    // 스크린 전체 로딩뷰
    LoadingView(isShow = movieDetailUiState.isLoading)
}

@Composable
fun ComposeMovieDetailUI(
    localContext: Context,
    scope: CoroutineScope,
    composeMovieDetailScreenEvent: (ComposeMovieDetailScreenEvent) -> Unit,
    composeMovieDetailViewModelEvent: (ComposeMovieDetailViewModelEvent) -> Unit,
    movieDetailUiState: MovieDetailUiState,
    movieReviewListPaging: LazyPagingItems<MovieReviewModel.Result>?
) {
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
                onSaveMovieClick = {
                    composeMovieDetailViewModelEvent.invoke(ComposeMovieDetailViewModelEvent.SaveMovieDetail(it))
                },
                onDeleteMovieClick = {
                    composeMovieDetailViewModelEvent.invoke(ComposeMovieDetailViewModelEvent.DeleteMovieDetail(it))
                },
                title = movieDetailUiState.movieDetail?.title?:"-",
                movieDetailUiState = movieDetailUiState
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // 영화 포스터
                item {
                    GlideImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f) // 3:4 비율 유지
                            .clip(MaterialTheme.shapes.extraSmall),
                        imageModel = {
                            BuildConfig.BASE_MOVIE_POSTER + movieDetailUiState.movieDetail?.posterPath
                        },
                        previewPlaceholder = painterResource(id = R.drawable.ic_search_24_000000),
                        loading = {
                            // 이미지 로딩 중 보여줄 뷰
                            Image(
                                painter = painterResource(id = R.drawable.ic_search_24_000000),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.None
                            )
                        },
                        imageOptions = ImageOptions(
                            alignment = Alignment.TopCenter,
                            contentScale = ContentScale.FillHeight
                        ),
                        failure = {
                            ImageEmptyView()
                        }
                    )
                }

                // 영화 제목
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
                        text = movieDetailUiState.movieDetail?.title ?: "Title Not Available",
                        style = TextStyle(
                            fontSize = dpToSp(dp = 20.dp),
                            fontWeight = FontWeight.SemiBold
                        ),
                        lineHeight = 30.sp
                    )
                }

                // 영화 줄거리
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 5.dp),
                        text = movieDetailUiState.movieDetail?.overview ?: "-",
                        style = TextStyle(fontSize = dpToSp(dp = 15.dp)),
                        lineHeight = 22.sp
                    )
                }

                // 리뷰 섹션 제목
                item {
                    if (movieReviewListPaging != null && movieReviewListPaging.itemCount > 0) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 30.dp, bottom = 15.dp, start = 20.dp, end = 20.dp),
                            text = "리뷰 (${movieReviewListPaging.itemCount})",
                            style = TextStyle(
                                fontSize = dpToSp(dp = 18.dp),
                                fontWeight = FontWeight.Bold
                            ),
                            lineHeight = 26.sp
                        )
                    }
                }

                // 리뷰 리스트 (Paging)
                movieReviewListPaging?.let { items ->
                    items(
                        count = items.itemCount
                    ) { index ->
                        items[index]?.let { review ->
                            ReviewItem(review = review)
                        }
                    }
                }

                // 빈 리뷰 상태
                item {
                    if (movieReviewListPaging == null || movieReviewListPaging.itemCount == 0 && !movieDetailUiState.isLoading) {
                        EmptyReviewView()
                    }
                }
            }
        }
    }


    BackHandler {
        composeMovieDetailScreenEvent.invoke(ComposeMovieDetailScreenEvent.OnBackClick())
    }
}

/**
 * 개별 리뷰 아이템 Composable
 */
@Composable
fun ReviewItem(
    review: MovieReviewModel.Result
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = grey50()
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 작성자 정보 Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 아바타 이미지 또는 기본 아이콘
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(deepPurple500()),
                    contentAlignment = Alignment.Center
                ) {
                    if (!review.authorDetails.avatarPath.isNullOrEmpty()) {
                        GlideImage(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            imageModel = {
                                if (review.authorDetails.avatarPath.startsWith("/")) {
                                    "https://image.tmdb.org/t/p/w200${review.authorDetails.avatarPath}"
                                } else {
                                    review.authorDetails.avatarPath
                                }
                            },
                            failure = {
                                Text(
                                    text = review.author.take(1).uppercase(),
                                    color = white(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        )
                    } else {
                        Text(
                            text = review.author.take(1).uppercase(),
                            color = white(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = review.author,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = grey900()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 별점 표시
                        if (review.authorDetails.rating > 0) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star_24_000000),
                                contentDescription = "Rating",
                                tint = yellowA400(),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${review.authorDetails.rating}/10",
                                fontSize = 12.sp,
                                color = grey600()
                            )
                        }
                    }
                }
            }

            // 리뷰 내용
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = review.content,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = grey800(),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )

            // 작성 날짜
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                text = formatDate(review.createdAt),
                fontSize = 12.sp,
                color = grey500()
            )
        }
    }
}

/**
 * 빈 리뷰 상태를 보여주는 Composable
 */
@Composable
fun EmptyReviewView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_search_24_000000),
            contentDescription = "No Reviews",
            tint = grey300(),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "No Reviews Found",
            fontSize = 16.sp,
            color = grey500(),
            fontWeight = FontWeight.Medium
        )

        Text(
            text = "Write your first review!",
            fontSize = 14.sp,
            color = grey300(),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

/**
 * 날짜 포맷팅 함수
 */
fun formatDate(dateString: String): String {
    return try {
        // ISO 8601 형태의 날짜를 간단한 형태로 변환
        // 예: "2023-12-01T10:30:00.000Z" -> "2023.12.01"
        val date = dateString.substring(0, 10).replace("-", ".")
        date
    } catch (e: Exception) {
        dateString
    }
}

@Composable
fun HeaderView(
    localContext: Context,
    onBackClick: () -> Unit,
    onSaveMovieClick: (MovieDetailModel) -> Unit,
    onDeleteMovieClick: (MovieDetailModel) -> Unit,
    title: String,
    movieDetailUiState: MovieDetailUiState
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
                text = title
            )
        }

        IconButton(
            modifier = Modifier
                .height(headerViewHeight.value.dp),
            onClick = {
                movieDetailUiState.movieDetail?.let {
                    if(movieDetailUiState.isSaveState) {
                        onDeleteMovieClick.invoke(it)
                    } else {
                        onSaveMovieClick.invoke(it)
                    }
                }
            }
        ) {
            Image(
                painter = if(movieDetailUiState.isSaveState){
                    painterResource(R.drawable.ic_star_24_000000)
                } else {
                    painterResource(R.drawable.ic_save_24_000000)
                },
                contentDescription = "SaveMovie"
            )
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

@Preview(showBackground = true)
@Composable
fun ComposeMovieDetailScreenPreview() {
    ComposeMovieDetailUI(
        localContext = LocalContext.current,
        scope = rememberCoroutineScope(),
        composeMovieDetailScreenEvent = {},
        composeMovieDetailViewModelEvent = {},
        movieDetailUiState = MovieDetailUiState(),
        movieReviewListPaging = flowOf(PagingData.from(
            listOf(
                MovieReviewModel.Result(
                    author = "John Doe",
                    authorDetails = MovieReviewModel.Result.AuthorDetails(
                        avatarPath = "/avatar1.jpg",
                        rating = 8,
                        name = "John Doe",
                        username = "johndoe123"
                    ),
                    content = "This is a great movie! I really enjoyed watching it.",
                    createdAt = "2023-12-01T10:30:00.000Z",
                    id = "review1",
                    updatedAt = "2023-12-01T11:00:00.000Z",
                    url = "https://example.com/review1"
                ),
                MovieReviewModel.Result(
                    author = "Jane Smith",
                    authorDetails = MovieReviewModel.Result.AuthorDetails(
                        avatarPath = "/avatar2.jpg",
                        rating = 7,
                        name = "Jane Smith",
                        username = "janesmith456"
                    ),
                    content = "The movie was okay, but I expected more from the storyline.",
                    createdAt = "2023-11-20T14:15:00.000Z",
                    id = "review2",
                    updatedAt = "2023-11-21T09:20:00.000Z",
                    url = "https://example.com/review2"
                )
            )
        )).collectAsLazyPagingItems()
    )
}