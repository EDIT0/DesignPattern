package com.example.presentation.ui.fragment

import android.app.ProgressDialog.show
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
import androidx.annotation.RequiresApi
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.presentation.R
import com.example.presentation.databinding.FragmentSavedMoviesBinding
import com.example.presentation.adapter.SavedMoviesAdapter
import com.example.presentation.ui.activity.MainActivity
import com.example.presentation.ui.activity.MovieInfoActivity
import com.example.presentation.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


class SavedMoviesFragment : Fragment() {

    private val CLASS_NAME = SavedMoviesFragment::class.java.simpleName

    lateinit var fragmentSavedMoviesBinding : FragmentSavedMoviesBinding

    lateinit var mainViewModel : MainViewModel
    private lateinit var savedMoviesAdapter: SavedMoviesAdapter

    private var isLoading = false

    lateinit var posts1Job : Job
    private var searchCoroutineJob: Job = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentSavedMoviesBinding = FragmentSavedMoviesBinding.bind(inflater.inflate(R.layout.fragment_saved_movies, container, false))
        return fragmentSavedMoviesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = (activity as MainActivity).mainViewModel
        savedMoviesAdapter = (activity as MainActivity).savedMoviesAdapter

        showProgressBar()
        initSavedMoviesRecyclerView()
        observeListener()
        searchListener()
        actionbarElevation()
    }

    private fun initSavedMoviesRecyclerView() {
        fragmentSavedMoviesBinding.rvSavedMoviesList.apply {
            adapter = savedMoviesAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SavedMoviesFragment.onScrollListener)
        }

        savedMoviesAdapter.setOnItemClickListener {
            val intent = Intent(requireActivity(), MovieInfoActivity::class.java)
            intent.putExtra("MovieInfo", it)
            requireActivity().startActivity(intent)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val movie = savedMoviesAdapter.currentList[position]

                mainViewModel.deleteSavedMovie(movie)

                Snackbar.make(view!!, "Delete Movie", Snackbar.LENGTH_LONG).apply {
                    setAction("되돌리기") {
                        mainViewModel.saveMovie(movie)
                    }
                    show()
                }
            }

        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(fragmentSavedMoviesBinding.rvSavedMoviesList)
        }
    }

    private fun observeListener() {
//        mainViewModel.getSavedMovies().observe(viewLifecycleOwner, {
//            savedMoviesAdapter.replaceItems(it)
//
//            if(it.size > 0) {
//                fragmentSavedMoviesBinding.tvDataEmpty.visibility = View.INVISIBLE
//            } else {
//                fragmentSavedMoviesBinding.tvDataEmpty.visibility = View.VISIBLE
//            }
//
//            hideProgressBar()
//        })

        mainViewModel.getSavedMovies.observe(viewLifecycleOwner, {
            Log.i(MainActivity.TAG + CLASS_NAME, "현재 저장된 아이템 갯수 : ${it.size}")
            savedMoviesAdapter.replaceItems(it)

            if(it.size > 0) {
                fragmentSavedMoviesBinding.tvDataEmpty.visibility = View.INVISIBLE
            } else {
                fragmentSavedMoviesBinding.tvDataEmpty.visibility = View.VISIBLE
            }

            hideProgressBar()
        })

        posts1Job = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.getSavedMoviesStateFlow.collectLatest {
                    Log.i("MYTAG", "저장된 영화 전체를 관찰 StateFlow ${it.size}")
                }
            }
        }

        posts1Job = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.getSearchSavedMoviesStateFlow.collectLatest {
                    Log.i("MYTAG", "저장된 영화들 중 현재 검색된 영화 관찰 StateFlow ${it.size}")
                }
            }
        }

    }

    private fun actionbarElevation() {
        if(Build.VERSION.SDK_INT > 23) {
            fragmentSavedMoviesBinding.rvSavedMoviesList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                fragmentSavedMoviesBinding.etSavedMovieSearch.isSelected = fragmentSavedMoviesBinding.rvSavedMoviesList.canScrollVertically(-1)
            }
        }
    }

    private fun showProgressBar() {
        isLoading = true
        fragmentSavedMoviesBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        fragmentSavedMoviesBinding.progressBar.visibility = View.INVISIBLE
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

            // 스크롤이 끝에 도달했는지 확인
            if (lastVisibleItemPosition == itemTotalCount) {

            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

        }
    }

    private fun searchListener() {
//        fragmentSavedMoviesBinding.etSavedMovieSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                if(p0?.length!! > 0 && !isLoading) {
//                    showProgressBar()
//
//                    MainScope().launch {
//                        delay(2000)
//                        mainViewModel.keyword.value = p0.toString()
//
//                    }
//                } else if(p0?.length!! == 0) {
//                    mainViewModel.keyword.value = p0.toString()
//
//                }
//            }
//
//        })
        // Rx의 스케줄러와 비슷
        // IO 스레드에서 돌리겠다
        searchCoroutineJob = lifecycleScope.launch {

            // editText 가 변경되었을때
            val editTextFlow = fragmentSavedMoviesBinding.etSavedMovieSearch.textChangesToFlow()

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
                    mainViewModel.keyword.value = it.toString()
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
}