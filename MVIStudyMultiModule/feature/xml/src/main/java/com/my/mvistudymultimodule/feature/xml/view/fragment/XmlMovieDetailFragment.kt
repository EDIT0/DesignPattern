package com.my.mvistudymultimodule.feature.xml.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.my.mvistudymultimodule.core.base.BaseDataBindingFragment
import com.my.mvistudymultimodule.core.util.LogUtil
import com.my.mvistudymultimodule.feature.xml.R
import com.my.mvistudymultimodule.feature.xml.databinding.FragmentXmlMainHomeBinding
import com.my.mvistudymultimodule.feature.xml.view.activity.XmlHomeActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class XmlMovieDetailFragment : BaseDataBindingFragment<FragmentXmlMainHomeBinding>(R.layout.fragment_xml_movie_detail) {

    private val activity by lazy {
        requireActivity() as XmlHomeActivity
    }

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

    }

    override fun onResume() {
        super.onResume()
        LogUtil.i_dev("${javaClass.simpleName} onResume()")
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