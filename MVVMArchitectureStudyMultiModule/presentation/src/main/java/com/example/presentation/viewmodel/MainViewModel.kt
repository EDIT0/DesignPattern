package com.example.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.domain.model.MovieModel
import com.example.domain.model.MovieModelResult
import com.example.data.util.NetworkManager
import com.example.data.util.SingleLiveEvent
import com.example.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(
    private val app : Application,
    private val networkManager: NetworkManager,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getSearchMoviesUseCase: GetSearchMoviesUseCase,
    private val getSavedMoviesUseCase: GetSavedMoviesUseCase,
    private val deleteSavedMovieUseCase: DeleteSavedMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val getSearchSavedMoviesUseCase: GetSearchSavedMoviesUseCase
) : AndroidViewModel(app) {

    private val _singleLiveEvent = SingleLiveEvent<Any>()
    val singleLiveEvent: LiveData<Any> get() = _singleLiveEvent

    val popularMovies : MutableLiveData<Response<MovieModel>> = MutableLiveData()
    fun getPopularMovies(language : String, page : Int){
        if(networkManager.checkNetworkState()) {
            viewModelScope.launch(Dispatchers.IO) {
                val apiResult = getPopularMoviesUseCase.execute(language, page)
                popularMovies.postValue(apiResult)
            }
        } else {
            _singleLiveEvent.call()
        }
    }

    val searchedMovies : MutableLiveData<Response<MovieModel>> = MutableLiveData()
    fun getSearchMovies(query : String, language: String, page: Int) {
        if(networkManager.checkNetworkState()) {
            viewModelScope.launch(Dispatchers.IO) {
                val apiResult = getSearchMoviesUseCase.execute(query, language, page)
                searchedMovies.postValue(apiResult)
            }
        } else {
            _singleLiveEvent.call()
        }
    }

//    fun getSavedMovies() = liveData {
//        getSavedMoviesUseCase.execute().collect {
//            emit(it)
//        }
//    }

    fun deleteSavedMovie(movieModelResult: MovieModelResult) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSavedMovieUseCase.execute(movieModelResult)
        }

    }

    fun saveMovie(movieModelResult: MovieModelResult) {
        viewModelScope.launch(Dispatchers.IO) {
            saveMovieUseCase.execute(movieModelResult)
        }
    }

    var keyword = MutableLiveData<String>("")
    val getSavedMovies: LiveData<List<MovieModelResult>> = Transformations.switchMap(keyword) { param->
        getSearchSavedMoviesUseCase.execute(param)
    }

    /**
     * StateFlow 저장된 영화 전체 관찰
     * */
    val getSavedMoviesStateFlow: StateFlow<List<MovieModelResult>> =
        getSavedMoviesUseCase.execute()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    /**
     * Flow 저장된 영화들 중 검색된 영화들만 관찰
     * */
    val getSearchSavedMoviesStateFlow: Flow<List<MovieModelResult>> = keyword.asFlow().flatMapLatest { filter ->
        getSearchSavedMoviesUseCase.execute_using_stateflow(filter)
    }

//    val getSearchSavedMoviesStateFlow = Transformations.switchMap(keyword) { param->
//        getSearchSavedMoviesUseCase.execute_using_stateflow(param)
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = emptyList<MovieModelResult>()
//            )
//    }
//    val getSearchSavedMoviesStateFlow =
//        getSearchSavedMoviesUseCase.execute_using_stateflow(keyword)
//            .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = emptyList<MovieModelResult>()
//        )



//    val getSearchSavedMoviesStateFlow: StateFlow<List<MovieModelResult>> =
//       getSearchSavedMoviesUseCase.execute_using_stateflow()
//           .mapLa
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = emptyList()
//            )
//    var getSavedMovies : MutableLiveData<List<MovieModelResult>> = MutableLiveData()
//    fun getSearchSavedMovies(keyword : String) {
////        Log.i("MYTAG", "뷰모델 ${keyword} ${it.size}")

//        getSearchSavedMoviesUseCase.execute(keyword).collect {
//            Log.i("MYTAG", "뷰모델 ${keyword} ${it.size}")
//            getSavedMovies.postValue(it)
//        }
//    }

//    fun getSearchSavedMovies(keyword : String) = liveData {
//        getSearchSavedMoviesUseCase.execute(keyword).collect {
//            emit(it)
//        }
//    }
}