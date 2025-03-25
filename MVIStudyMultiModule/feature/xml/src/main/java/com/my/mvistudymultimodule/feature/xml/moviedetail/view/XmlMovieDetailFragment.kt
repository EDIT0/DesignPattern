package com.my.mvistudymultimodule.feature.xml.moviedetail.view

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.my.mvistudymultimodule.core.base.BaseDataBindingFragment
import com.my.mvistudymultimodule.core.base.INTENT_KEY_MOVIE_ID
import com.my.mvistudymultimodule.core.di.BuildConfig
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClick
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.xml.R
import com.my.mvistudymultimodule.feature.xml.databinding.FragmentXmlMovieDetailBinding
import com.my.mvistudymultimodule.feature.xml.home.view.XmlHomeActivity
import com.my.mvistudymultimodule.feature.xml.moviedetail.event.MovieDetailErrorUiEvent
import com.my.mvistudymultimodule.feature.xml.moviedetail.event.XmlMovieDetailViewModelEvent
import com.my.mvistudymultimodule.feature.xml.moviedetail.viewmodel.XmlMovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class XmlMovieDetailFragment : BaseDataBindingFragment<FragmentXmlMovieDetailBinding>(R.layout.fragment_xml_movie_detail) {

    private val xmlMovieDetailVM: XmlMovieDetailViewModel by viewModels()
    private lateinit var scope: LifecycleCoroutineScope
    private lateinit var activity: Activity

    private var backPressCallback: OnBackPressedCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val resultBundle = Bundle().apply {
                    putString("resultStringKey", "Result from XmlMovieDetailFragment")
                    putInt("resultIntKey", 123)
                }
                setFragmentResult("requestKey", resultBundle)
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LogUtil.i_dev("${javaClass.simpleName} onViewCreated()")

        scope = viewLifecycleOwner.lifecycleScope
        activity = requireActivity() as XmlHomeActivity

        if(xmlMovieDetailVM.movieDetailUiEvent.value.movieDetail == null) {
            val movieId = arguments?.getInt(INTENT_KEY_MOVIE_ID)
            movieId?.let {
                xmlMovieDetailVM.handleViewModelEvent(XmlMovieDetailViewModelEvent.SetMovieId(movieId))
                xmlMovieDetailVM.handleViewModelEvent(XmlMovieDetailViewModelEvent.GetMovieDetail())
            }?: also {
                findNavController().popBackStack()
            }
        }

        clickListener()
        viewObserver()
    }

    private fun clickListener() {
        binding.ibBack.onSingleClick(1000L, {
            val resultBundle = Bundle().apply {
                putString("resultStringKey", "Result from XmlMovieDetailFragment")
                putInt("resultIntKey", 123)
            }
            setFragmentResult("requestKey", resultBundle)
            findNavController().popBackStack()
        })

        binding.ivMovieThumbnail.onSingleClick(1000L) {
            findNavController().navigate(R.id.action_xmlMovieDetailFragment_to_xmlMovieSearchFragment, args = null, navOptions = null)
        }
    }

    private fun viewObserver() {
        scope.launch {
            xmlMovieDetailVM.movieDetailUiEvent.collect {
                (activity as XmlHomeActivity).showCommonLoading(it.isLoading)

                LogUtil.i_dev("영화 상세정보: ${it.movieDetail}")

                it.movieDetail?.let { movie ->
                    binding.tvMovieTitle.text = movie.title

                    Glide.with(binding.ivMovieThumbnail.context)
                        .load(BuildConfig.BASE_MOVIE_POSTER + movie.posterPath)
                        .error(com.my.mvistudymultimodule.core.base.R.drawable.ic_search_24_000000)
                        .listener((object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        }))
                        .into(binding.ivMovieThumbnail)

                    binding.tvMovieOriginalTitle.text = movie.originalTitle
                    binding.tvMovieReleaseDate.text = movie.releaseDate
                    binding.tvMovieOverview.text = movie.overview
                }
            }
        }

        scope.launch {
            xmlMovieDetailVM.movieDetailErrorUiEvent.collect {
                when(it) {
                    is MovieDetailErrorUiEvent.ConnectionError -> {
                        LogUtil.d_dev("ConnectionError ConnectionError: ${it.code}")
                    }
                    is MovieDetailErrorUiEvent.DataEmpty -> {
                        LogUtil.d_dev("ConnectionError DataEmpty: ${it.isDataEmpty}")
                    }
                    is MovieDetailErrorUiEvent.ExceptionHandle -> {
                        LogUtil.d_dev("ConnectionError ExceptionHandle: ${it.throwable}")
                    }
                    is MovieDetailErrorUiEvent.Fail -> {
                        LogUtil.d_dev("ConnectionError Fail: ${it.code}")
                    }
                    is MovieDetailErrorUiEvent.Idle -> {
                        LogUtil.d_dev("ConnectionError Idle")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtil.i_dev("${javaClass.simpleName} onResume()")

        scope.launch {
            delay(100L)
            binding.svMovieDetail.smoothScrollTo(0, xmlMovieDetailVM.getScrollPosition())
        }
    }

    override fun onStop() {
        super.onStop()
        LogUtil.i_dev("${javaClass.simpleName} onStop()")

        xmlMovieDetailVM.apply {
            setScrollPosition(binding.svMovieDetail.scrollY)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        backPressCallback?.remove()
        super.onDetach()
    }
}