package com.example.mvvmarchitecturestudy2.presentation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleOwner
import com.example.mvvmarchitecturestudy2.R
import com.example.mvvmarchitecturestudy2.databinding.ActivityMovieInfoBinding
import com.example.mvvmarchitecturestudy2.presentation.adapter.MovieReviewAdapter
import com.example.mvvmarchitecturestudy2.presentation.base.BaseActivity
import com.example.mvvmarchitecturestudy2.presentation.ui.fragment.InfoFragment
import com.example.mvvmarchitecturestudy2.presentation.ui.fragment.PosterFragment
import com.example.mvvmarchitecturestudy2.presentation.ui.fragment.ReviewFragment
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MainViewModel
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MovieInfoViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// https://api.themoviedb.org/3/movie/361743?api_key=apikey&language=en-US 영화 디테일
// https://api.themoviedb.org/3/movie/361743/reviews?api_key=apikey&language=en-US&page=1 영화 리뷰

@AndroidEntryPoint
class MovieInfoActivity : BaseActivity<ActivityMovieInfoBinding>(R.layout.activity_movie_info) {

    private val TAG = MovieInfoActivity::class.simpleName

    @Inject
    lateinit var movieInfoViewModel: MovieInfoViewModel

    @Inject
    lateinit var movieReviewAdapter: MovieReviewAdapter

    private lateinit var posterFragment: PosterFragment
    private lateinit var infoFragment: InfoFragment
    private lateinit var reviewFragment: ReviewFragment

    private var isPosterFragmentLoad = true
    private var isInfoFragmentLoad = true
    private var isReviewFragmentLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "MovieInfoViewModel HashCode: ${movieInfoViewModel.hashCode()}")

        binding.vm = movieInfoViewModel

        movieInfoViewModel.setMovieId(intent.getIntExtra("movieId", 0))
        Log.i(TAG, "${movieInfoViewModel.getMovieId()}")

        initFragment()
        viewPagerAndTabLayoutSetting()
        viewModelToast()
        val tab: TabLayout.Tab? = binding.tablayout.getTabAt(0)
        tab?.select()
    }

    private fun initFragment() {
        posterFragment = PosterFragment()
        infoFragment = InfoFragment()
        reviewFragment = ReviewFragment()
    }

    private fun viewPagerAndTabLayoutSetting() {
        binding.vpMain.setOffscreenPageLimit(3) //화면 갯수 설정
        val viewPagerAdapter = viewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addItemfragment(posterFragment)
        viewPagerAdapter.addItemfragment(infoFragment)
        viewPagerAdapter.addItemfragment(reviewFragment)

        binding.vpMain.adapter = viewPagerAdapter

        val tab_mainFragment = binding.tablayout.newTab().setText("포스터")
        binding.tablayout.addTab(tab_mainFragment)
        val tab_mainSounddoodleListFragment = binding.tablayout.newTab().setText("정보")
        binding.tablayout.addTab(tab_mainSounddoodleListFragment)
        val tab_mainDoodleListFragment = binding.tablayout.newTab().setText("리뷰")
        binding.tablayout.addTab(tab_mainDoodleListFragment)

        // 탭 가리기
//        binding.toolbar.getTabAt(0)?.view?.visibility = View.GONE

        binding.vpMain.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tablayout))
        binding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 선택되지 않았던 탭이 선택되었을 경우
                Log.i(TAG, "선택된 탭: ${tab?.position}")
                binding.vpMain.currentItem = tab?.position!!

//                if(tab.position == 0) {
//                    if(isPosterFragmentLoad) {
//                        isPosterFragmentLoad = false
//                        movieINfoViewModel.getPoster()
//                    }
//                } else if(tab.position == 1) {
//                    if(isInfoFragmentLoad) {
//                        isInfoFragmentLoad = false
//                        movieINfoViewModel.getInfo()
//                    }
//                } else if(tab.position == 2) {
//                    if(isReviewFragmentLoad) {
//                        isReviewFragmentLoad = false
//                        movieINfoViewModel.getReview()
//                    }
//                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택된 상태에서 선택되지 않음으로 변경될 경우
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택되었을 경우

//                if(tab?.position == 0) {
//                    if(isPosterFragmentLoad) {
//                        isPosterFragmentLoad = false
//                        movieINfoViewModel.getPoster()
//                    }
//                } else if(tab?.position == 1) {
//                    if(isInfoFragmentLoad) {
//                        isInfoFragmentLoad = false
//                        movieINfoViewModel.getInfo()
//                    }
//                } else if(tab?.position == 2) {
//                    if(isReviewFragmentLoad) {
//                        isReviewFragmentLoad = false
//                        movieINfoViewModel.getReview()
//                    }
//                }
            }

        })
    }

    inner class viewPagerAdapter(
        fragmentManager: FragmentManager
    ) : FragmentStatePagerAdapter(fragmentManager) {

        var items: ArrayList<Fragment> = ArrayList<Fragment>() //프래그먼트 화면을 담아둘 배열


        //ArrayList에 add로 화면 추가하는 메소드
        fun addItemfragment(item: Fragment) {
            items.add(item)
        }

        override fun getCount(): Int = items.size


        override fun getItem(position: Int): Fragment = items.get(position)
    }

    private fun viewModelToast() {
        with(movieInfoViewModel) {
            toastMsg.observe(mActivity as LifecycleOwner, {
                when (toastMsg.value) {
                    MovieInfoViewModel.MessageSet.SUCCESS -> showToast(getString(R.string.toast_success))
                    MovieInfoViewModel.MessageSet.NO_DATA -> showToast(getString(R.string.toast_no_data))
                    MovieInfoViewModel.MessageSet.ERROR -> showToast(getString(R.string.toast_error))
                    MovieInfoViewModel.MessageSet.NETWORK_NOT_CONNECTED -> showToast(getString(R.string.toast_network_disconnect))
                    MovieInfoViewModel.MessageSet.NO_SEARCH -> showToast(getString(R.string.toast_no_search))
                    MovieInfoViewModel.MessageSet.SEARCH_SUCCESS -> { }
                }
            })
        }
    }
}