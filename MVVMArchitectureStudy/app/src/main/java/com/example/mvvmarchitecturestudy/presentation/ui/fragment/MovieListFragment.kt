package com.example.mvvmarchitecturestudy.presentation.ui.fragment

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecturestudy.R
import com.example.mvvmarchitecturestudy.data.model.MovieModelResult
import com.example.mvvmarchitecturestudy.databinding.FragmentMovieListBinding
import com.example.mvvmarchitecturestudy.presentation.adapter.MovieAdapter
import com.example.mvvmarchitecturestudy.presentation.ui.activity.MainActivity
import com.example.mvvmarchitecturestudy.presentation.ui.activity.MainActivity.Companion.TAG
import com.example.mvvmarchitecturestudy.presentation.ui.activity.MovieInfoActivity
import com.example.mvvmarchitecturestudy.presentation.viewmodel.MainViewModel


class MovieListFragment : Fragment() {
    private val CLASS_NAME = MovieListFragment::class.java.simpleName

    lateinit var fragmentMovieListBinding: FragmentMovieListBinding
    lateinit var mainViewModel : MainViewModel
    private lateinit var movieAdapter: MovieAdapter

    private var language = "en-US"
    private var isLoading = false

    private var page = 1
    private var totalPages = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        fragmentMovieListBinding = FragmentMovieListBinding.bind(inflater.inflate(R.layout.fragment_movie_list, container, false))

        return fragmentMovieListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = (activity as MainActivity).mainViewModel
        movieAdapter = (activity as MainActivity).movieAdapter

        initRecyclerView()
        initData()
        observeListener()
        actionbarElevation()
    }

    private fun initRecyclerView() {
        fragmentMovieListBinding.rvMoviesList.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@MovieListFragment.onScrollListener)
        }

        movieAdapter.setOnItemClickListener { view, adapterPosition, it ->
//            val intent = Intent(requireActivity(), MovieInfoActivity::class.java)
////            val bundle = Bundle().apply {
////                putSerializable("MovieInfo", it)
////            }
//            Log.i(MainActivity.TAG + CLASS_NAME, "${adapterPosition} ${it}")
//            intent.putExtra("MovieInfo", it)
////            resultLauncher?.launch(intent)
//            requireActivity().startActivity(intent)


            // Check if we're running on Android 5.0 or higher
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Apply activity transition
            } else {
                // Swap without transition
            }

            val intent = Intent(requireActivity(), MovieInfoActivity::class.java)
            intent.putExtra("MovieInfo", it)
//            val thumbView: View = view.findViewById(R.id.iv_thumbnail)
//            val pair_thumb: androidx.core.util.Pair<View, String> = androidx.core.util.Pair.create(thumbView, thumbView.transitionName)
//            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), pair_thumb)
            val options : ActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                android.util.Pair.create(view.ivThumbnail, "thumbnailTransition"),
                android.util.Pair.create(view.tvTitle, "titleTransition"),
//                        android.util.Pair.create(sounddoodleListItemBinding.txtName, "nickname")
            )
            requireActivity().startActivity(intent, options.toBundle())

            ""
        }
    }

    private fun initData() {
        showProgressBar()
        mainViewModel.getPopularMovies("en-US", 1)
    }

    private fun observeListener() {
        mainViewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            Log.i(TAG + CLASS_NAME, "전체 영화 : ${it.body()}")
            totalPages = it.body()?.totalPages?:0

            if(it.isSuccessful && it.body()?.movieModelResults != null) {
                val currentList : List<MovieModelResult> = movieAdapter.currentList
                val newList : List<MovieModelResult> = it.body()?.movieModelResults!!
                val updateList = currentList + newList
                movieAdapter.replaceItems(updateList)
            } else {

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
            fragmentMovieListBinding.rvMoviesList.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                fragmentMovieListBinding.linearLayout.isSelected = fragmentMovieListBinding.rvMoviesList.canScrollVertically(-1)
            }
        }
    }



    private fun showProgressBar() {
        isLoading = true
        fragmentMovieListBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        isLoading = false
        fragmentMovieListBinding.progressBar.visibility = View.INVISIBLE
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
                    if(totalPages != page && movieAdapter.currentList.size > 0) {
                        page++
                        showProgressBar()
                        mainViewModel.getPopularMovies(language, page)
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }
}