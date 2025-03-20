package com.my.mvistudymultimodule.feature.xml.mainhome.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat.finishAfterTransition
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.my.mvistudymultimodule.core.base.BaseDataBindingFragment
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClick
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClickWithDebounce
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.xml.R
import com.my.mvistudymultimodule.feature.xml.databinding.FragmentXmlMainHomeBinding
import com.my.mvistudymultimodule.feature.xml.home.view.XmlHomeActivity
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListErrorUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.event.XmlMainHomeViewModelEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.view.adapter.LoadStateAdapter
import com.my.mvistudymultimodule.feature.xml.mainhome.viewmodel.XmlMainHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class XmlMainHomeFragment : BaseDataBindingFragment<FragmentXmlMainHomeBinding>(R.layout.fragment_xml_main_home)  {

    private val xmlMainHomeVM: XmlMainHomeViewModel by viewModels()
    private lateinit var scope: LifecycleCoroutineScope
    private lateinit var activity: Activity

    private var backPressCallback: OnBackPressedCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent()
                intent.putExtra("Data", "KO, US, UK")
                activity.setResult(0, intent)
//                activity.finish()
                finishAfterTransition(activity)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LogUtil.i_dev("${javaClass.simpleName} onViewCreated()")

        scope = viewLifecycleOwner.lifecycleScope
        activity = requireActivity() as XmlHomeActivity

        resultListener()
        intentDataListener()
        initRvAndAdapter()
        clickListener()
        viewObserver()

    }

    private fun resultListener() {
        setFragmentResultListener("requestKey") { _, bundle ->
            val resultStringData = bundle.getString("resultStringKey") // "Result from XmlMainHomeFragment"
            val resultIntData = bundle.getInt("resultIntKey") // "Result from XmlMainHomeFragment"
            LogUtil.d_dev("resultData : ${resultStringData} / ${resultIntData}")
        }
    }

    private fun intentDataListener() {
        val dataBundle = activity.intent.extras
        if (dataBundle != null) {
            val stringValue = dataBundle.getString("key1")
            val intValue = dataBundle.getInt("key2")

            LogUtil.d_dev("String value: $stringValue")
            LogUtil.d_dev("Int value: $intValue")
        }
    }

    private fun initRvAndAdapter() {
        binding.rvMovie.apply {
            adapter = xmlMainHomeVM.movieListPagingAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }

        xmlMainHomeVM.movieListPagingAdapter.addLoadStateListener {
//            LogUtil.i_dev("prepend Loading ${it.source.prepend is LoadState.Loading}")
//            LogUtil.i_dev("append Loading ${it.source.append is LoadState.Loading}")
//            LogUtil.i_dev("refresh Loading ${it.source.refresh is LoadState.Loading}")

            LogUtil.i_dev("addLoadStateListener ${it}")

            when(it.source.append) {
                is LoadState.Error -> {
                    (activity as XmlHomeActivity).showCommonLoading(false)
                }
                is LoadState.Loading -> {
                    (activity as XmlHomeActivity).showCommonLoading(true)
                }
                is LoadState.NotLoading -> {
                    (activity as XmlHomeActivity).showCommonLoading(false)
                }
            }

            when(it.source.prepend) {
                is LoadState.Error -> {

                }
                is LoadState.Loading -> {

                }
                is LoadState.NotLoading -> {

                }
            }

            when(it.source.refresh) {
                is LoadState.Error -> {
                    (activity as XmlHomeActivity).showCommonLoading(false)

                    if(xmlMainHomeVM.movieListPagingAdapter.itemCount == 0) {
                        binding.tvMovieListEmpty.visibility = View.VISIBLE
                        binding.rvMovie.visibility = View.GONE
                    }
                }
                is LoadState.Loading -> {
                    (activity as XmlHomeActivity).showCommonLoading(true)
                    binding.tvMovieListEmpty.visibility = View.GONE
                    binding.rvMovie.visibility = View.VISIBLE
                }
                is LoadState.NotLoading -> {

                }
            }

//            if(it.source.refresh is LoadState.Loading) {
//                LogUtil.d_dev("첫 로딩 중 일 때")
//            } else {
//                LogUtil.d_dev("로딩 중이 아닐 때")
//
//                binding.tvMovieListEmpty.visibility = View.GONE
//                binding.rvMovie.visibility = View.VISIBLE
//
//                val errorState = when {
//                    it.prepend is LoadState.Error -> {
//                        LogUtil.d_dev("페이징 prepend 에러: ${it.prepend}")
//                        it.prepend as LoadState.Error
//                    }
//                    it.append is LoadState.Error -> {
//                        LogUtil.d_dev("페이징 append 에러: ${it.append}")
//                        it.append as LoadState.Error
//                    }
//                    it.refresh is LoadState.Error -> {
//                        LogUtil.d_dev("페이징 refresh 에러: ${it.refresh}")
//
//                        if(xmlMainHomeVM.movieListPagingAdapter.itemCount == 0) {
//                            binding.tvMovieListEmpty.visibility = View.VISIBLE
//                            binding.rvMovie.visibility = View.GONE
//                        }
//
//                        it.refresh as LoadState.Error
//                    }
//                    else -> null
//                }

//                LogUtil.d_dev("페이징 에러: ${errorState}")
//                val errorMessage = errorState?.error?.message
//            }
        }

        // retry 말그대로 실패 후 재시도
        binding.apply {
            rvMovie.setHasFixedSize(true) // 사이즈 고정
            // header, footer 설정
            rvMovie.adapter = xmlMainHomeVM.movieListPagingAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter(
                    retry = {
                        xmlMainHomeVM.movieListPagingAdapter.retry()
                    }
                ),
                footer = LoadStateAdapter(
                    retry = {
                        xmlMainHomeVM.movieListPagingAdapter.retry()
                    }
                )
            )
        }

        xmlMainHomeVM.movieListPagingAdapter.setOnItemClickListener { i, movie ->
            LogUtil.i_dev("${i}번째 데이터 ${movie.title}")
        }
    }

    private fun clickListener() {
        binding.ibBack.onSingleClick(1000L, {
            val intent = Intent()
            intent.putExtra("Data", "KO, US, UK")
            activity.setResult(0, intent)
//                activity.finish()
            finishAfterTransition(activity)
        })

        binding.tvMovieSearch.onSingleClickWithDebounce(
            onClicked = {
                goToMovieDetail()
            },
            debounceTime = 1000L
        ).launchIn(scope)

        binding.ibMovieSearch.onSingleClickWithDebounce(
            onClicked = {
                goToMovieDetail()
            },
            debounceTime = 1000L
        ).launchIn(scope)

        binding.tvMovieListEmpty.onSingleClick(1000L, {
            xmlMainHomeVM.movieListPagingAdapter.retry()
        })
    }

    private fun viewObserver() {
        scope.launch {
            xmlMainHomeVM.movieListUiState.collect {
                if(it.isLoading) {
                    LogUtil.d_dev("뷰 업데이트 isLoading: ${it.isLoading}")
                } else {
                    LogUtil.d_dev("뷰 업데이트 isLoading: ${it.isLoading}")
                }

                if(it.movieList.isNullOrEmpty()) {
                    LogUtil.d_dev("뷰 업데이트 movieList: ${it.movieList}")
                } else {
                    LogUtil.d_dev("뷰 업데이트 movieList: ${it.movieList[0].title}")
                }
            }
        }

        scope.launch {
            xmlMainHomeVM.movieListErrorUiEvent.collect {
                when(it) {
                    is MovieListErrorUiEvent.ConnectionError -> {
                        LogUtil.d_dev("ConnectionError ConnectionError: ${it.code}")
                    }
                    is MovieListErrorUiEvent.DataEmpty -> {
                        LogUtil.d_dev("ConnectionError DataEmpty: ${it.isDataEmpty}")
                        if(xmlMainHomeVM.movieListUiState.value.movieList.isNullOrEmpty()) {
                            binding.tvMovieListEmpty.visibility = View.VISIBLE
                            binding.rvMovie.visibility = View.GONE
                        } else {
                            binding.tvMovieListEmpty.visibility = View.GONE
                            binding.rvMovie.visibility = View.VISIBLE
                        }
                    }
                    is MovieListErrorUiEvent.ExceptionHandle -> {
                        LogUtil.d_dev("ConnectionError ExceptionHandle: ${it.throwable}")
                        if(xmlMainHomeVM.movieListUiState.value.movieList.isNullOrEmpty()) {
                            binding.tvMovieListEmpty.visibility = View.VISIBLE
                            binding.rvMovie.visibility = View.GONE
                        } else {
                            binding.tvMovieListEmpty.visibility = View.GONE
                            binding.rvMovie.visibility = View.VISIBLE
                        }
                    }
                    is MovieListErrorUiEvent.Fail -> {
                        LogUtil.d_dev("ConnectionError Fail: ${it.code}")
                    }
                    is MovieListErrorUiEvent.Idle -> {
                        LogUtil.d_dev("ConnectionError Idle")
                    }
                }
            }
        }

        scope.launch {
            xmlMainHomeVM.movieListPagingUiState.collect {
                if(it.isLoading) {
                    LogUtil.d_dev("뷰 업데이트 isLoading: ${it.isLoading}")
                } else {
                    LogUtil.d_dev("뷰 업데이트 isLoading: ${it.isLoading}")
                }

                launch {
                    it.movieList?.let { movieList ->
                        delay(100L)
                        xmlMainHomeVM.movieListPagingAdapter.submitData(this@XmlMainHomeFragment.lifecycle, movieList)
                    }
                }
            }
        }
    }

    private fun goToMovieDetail() {
        /**
         * Fragment To Fragment 이동 및 데이터 전달 예시
         */
        val dataString = "Hello from SplashStartFragment"
        val dataInt = 42

        val bundle = Bundle().apply {
            putString("dataString", dataString)
            putInt("dataInt", dataInt)
        }

        findNavController().navigate(R.id.action_xmlMainHomeFragment_to_xmlMovieDetailFragment, args = bundle, navOptions = null)
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i_dev("${javaClass.simpleName} onResume()")

        if(xmlMainHomeVM.movieListPagingAdapter.itemCount == 0) {
            xmlMainHomeVM.handleViewModelEvent(XmlMainHomeViewModelEvent.GetPopularMovie())
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtil.i_dev("${javaClass.simpleName} onStop()")
    }

    override fun onDetach() {
        backPressCallback?.remove()
        super.onDetach()
    }
}