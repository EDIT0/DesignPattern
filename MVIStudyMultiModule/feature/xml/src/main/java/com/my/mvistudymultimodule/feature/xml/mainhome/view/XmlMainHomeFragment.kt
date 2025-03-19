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
import com.my.mvistudymultimodule.core.base.BaseDataBindingFragment
import com.my.mvistudymultimodule.core.util.ClickUtil.onSingleClickWithDebounce
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.xml.R
import com.my.mvistudymultimodule.feature.xml.databinding.FragmentXmlMainHomeBinding
import com.my.mvistudymultimodule.feature.xml.home.view.XmlHomeActivity
import com.my.mvistudymultimodule.feature.xml.mainhome.event.MovieListErrorUiEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.event.XmlMainHomeViewModelEvent
import com.my.mvistudymultimodule.feature.xml.mainhome.viewmodel.XmlMainHomeViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    private fun clickListener() {
        binding.ibBack.onSingleClickWithDebounce(
            onClicked = {
                val intent = Intent()
                intent.putExtra("Data", "KO, US, UK")
                activity.setResult(0, intent)
//                activity.finish()
                finishAfterTransition(activity)
            },
            debounceTime = 1000L
        ).launchIn(scope)

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

        if(xmlMainHomeVM.movieListUiState.value.movieList.isNullOrEmpty()) {
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