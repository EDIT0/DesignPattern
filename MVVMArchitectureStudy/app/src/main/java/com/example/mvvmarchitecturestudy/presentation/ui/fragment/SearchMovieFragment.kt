package com.example.mvvmarchitecturestudy.presentation.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecturestudy.R
import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.databinding.FragmentSearchMovieBinding
import com.example.mvvmarchitecturestudy.presentation.adapter.SearchMovieAdapter
import com.example.mvvmarchitecturestudy.presentation.ui.activity.MainActivity
import com.example.mvvmarchitecturestudy.presentation.ui.activity.MovieInfoActivity
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.onStart
import kotlin.coroutines.CoroutineContext


class SearchMovieFragment : Fragment() {

    private val CLASS_NAME = SearchMovieFragment::class.java.simpleName

    lateinit var fragmentSearchMovieBinding: FragmentSearchMovieBinding
    lateinit var mainViewModel : MainViewModel
    private lateinit var searchMovieAdapter : SearchMovieAdapter

    private var language = "en-US"
    private var isLoading = false

    private var page = 1
    private var totalPages = 0

    private var searchCoroutineJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(MainActivity.TAG + CLASS_NAME, "onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSearchMovieBinding = FragmentSearchMovieBinding.bind(inflater.inflate(R.layout.fragment_search_movie, container, false))

        return fragmentSearchMovieBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = (activity as MainActivity).mainViewModel
        searchMovieAdapter = (activity as MainActivity).searchMovieAdapter


        initSearchMovieRecyclerView()
        observeListener()
        searchListener()
        actionbarElevation()
    }

    private fun initSearchMovieRecyclerView() {
        fragmentSearchMovieBinding.rvMoviesList.apply {
            adapter = searchMovieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchMovieFragment.onScrollListener)
        }

        searchMovieAdapter.setOnItemClickListener {
            val intent = Intent(requireActivity(), MovieInfoActivity::class.java)
            intent.putExtra("MovieInfo", it)
            requireActivity().startActivity(intent)
        }
    }

    private fun observeListener() {
        mainViewModel.searchedMovies.observe(viewLifecycleOwner, Observer {
            Log.i(MainActivity.TAG + CLASS_NAME, "검색된 영화 : ${it.body()}")
            totalPages = it.body()?.totalPages?:0

            if(it.isSuccessful && it.body()?.movieModelResults?.size?:0 > 0) {
                fragmentSearchMovieBinding.tvDataEmpty.visibility = View.INVISIBLE

                val currentList : List<MovieModelResult> = searchMovieAdapter.currentList
                val newList : List<MovieModelResult> = it.body()?.movieModelResults!!
                val updateList = currentList + newList
                searchMovieAdapter.replaceItems(updateList)
            } else if(searchMovieAdapter.currentList.size == 0) {
                fragmentSearchMovieBinding.tvDataEmpty.visibility = View.VISIBLE
            }

            hideProgressBar()
        })

        mainViewModel.singleLiveEvent.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Check your network", Toast.LENGTH_SHORT).show()
            hideProgressBar()
            if(page > 1) {
                page--
            }
        })
    }

    private fun actionbarElevation() {
        if(Build.VERSION.SDK_INT > 23) {
            fragmentSearchMovieBinding.rvMoviesList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                fragmentSearchMovieBinding.etMovieSearch.isSelected = fragmentSearchMovieBinding.rvMoviesList.canScrollVertically(-1)
            }
        }
    }

    private fun showProgressBar() {
        isLoading = true
        fragmentSearchMovieBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        fragmentSearchMovieBinding.progressBar.visibility = View.INVISIBLE
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

            // 스크롤이 끝에 도달했는지 확인
            if (lastVisibleItemPosition == itemTotalCount) {
                if(isLoading) {

                } else {
                    if(totalPages != page && searchMovieAdapter.currentList.size > 0) {
                        page++
                        showProgressBar()
                        mainViewModel.getSearchMovies(fragmentSearchMovieBinding.etMovieSearch.text.toString(), language, page)
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }
    }

    private fun searchListener() {
//        fragmentSearchMovieBinding.etMovieSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                page = 1
//
//                if(p0?.length!! > 0 && !isLoading) {
//                    showProgressBar()
//
//                    MainScope().launch {
//                        delay(2000)
//
//
//                        Log.i(MainActivity.TAG + CLASS_NAME, "검색어는 ? ${p0} / 로딩상태 : ${isLoading}")
//                        searchMovieAdapter.submitList(emptyList())
//
//                        mainViewModel.getSearchMovies(p0.toString(), language, page)
//
//                    }
//                } else if(p0?.length!! == 0) {
//                }
//            }
//
//        })
        // Rx의 스케줄러와 비슷
        // IO 스레드에서 돌리겠다
        searchCoroutineJob = lifecycleScope.launch {

            // editText 가 변경되었을때
            val editTextFlow = fragmentSearchMovieBinding.etMovieSearch.textChangesToFlow()

            editTextFlow
                // 연산자들
                // 입력되고 나서 1초 뒤에 받는다
                .debounce(1000)
                .filter {
                    it?.length!! > 0
                }
                .onEach {
                    Log.d(MainActivity.TAG + CLASS_NAME, "flow로 받는다 $it")
                    // 해당 검색어로 api 호출
                    Log.i(MainActivity.TAG + CLASS_NAME, "검색어는 ? ${it} / 로딩상태 : ${isLoading}")
                    searchMovieAdapter.submitList(emptyList())

                    mainViewModel.getSearchMovies(it.toString(), language, page)
                }
                .launchIn(this) // Main이 아닌 별도 스레드로 수행
        }
    }


    fun EditText.textChangesToFlow(): Flow<CharSequence?> {
        return callbackFlow {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                    Unit

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    //offer(text)
                    // 값 내보내기
                    trySend(text)
                }
            }
            addTextChangedListener(listener)
            // 콜백이 사라질때 실행, 리스너 제거
            awaitClose { removeTextChangedListener(listener) }
        }.onStart {
            // event 방출
            emit(text)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}