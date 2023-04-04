package com.example.mvvmarchitecturestudy2.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmarchitecturestudy2.R
import com.example.mvvmarchitecturestudy2.databinding.FragmentReviewBinding
import com.example.mvvmarchitecturestudy2.presentation.adapter.MovieAdapter
import com.example.mvvmarchitecturestudy2.presentation.adapter.MovieReviewAdapter
import com.example.mvvmarchitecturestudy2.presentation.ui.activity.MovieInfoActivity
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MovieInfoViewModel
import javax.inject.Inject

class ReviewFragment : Fragment() {

    private val TAG = ReviewFragment::class.simpleName

    private lateinit var fragmentReviewBinding: FragmentReviewBinding

    lateinit var movieInfoViewModel: MovieInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView() ${this.hashCode()}")
        fragmentReviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false)
        return fragmentReviewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated()")

        movieInfoViewModel = (activity as MovieInfoActivity).movieInfoViewModel

        Log.i(TAG, "페이지: ${movieInfoViewModel.getReviewPage()}")
        movieInfoViewModel.getMovieReview()
        initRecyclerView()
        observeListener()
    }

    private fun initRecyclerView() {
        fragmentReviewBinding.rvReview.apply {
            adapter = (activity as MovieInfoActivity).movieReviewAdapter
            layoutManager = LinearLayoutManager(requireActivity().applicationContext)
            addOnScrollListener(onScrollListener)
        }

//        movieAdapter.setOnItemClickListener { position, movieModelResult ->
//            val intent = Intent(mActivity, MovieInfoActivity::class.java)
//            intent.putExtra("movieId", mainViewModel.movieList.value?.get(position)?.id)
//            startActivity(intent)
//        }
    }

    private fun observeListener() {
        movieInfoViewModel.movieReviewList.observe(viewLifecycleOwner, {
            Log.i(TAG, "value : ${it}")
//            (activity as MovieInfoActivity).movieReviewAdapter.submitList(it.toMutableList())

            fragmentReviewBinding.vm = movieInfoViewModel

        })
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
            val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

            // 스크롤이 끝에 도달했는지 확인
            if (lastVisibleItemPosition == itemTotalCount) {
                if (movieInfoViewModel.isLoading.value == true) {

                } else {
                    if (movieInfoViewModel.getReviewTotalPages() != movieInfoViewModel.getReviewPage() && movieInfoViewModel.movieReviewList .value?.size!! > 0) {
                        movieInfoViewModel.getMovieReview()
                    }
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }
}