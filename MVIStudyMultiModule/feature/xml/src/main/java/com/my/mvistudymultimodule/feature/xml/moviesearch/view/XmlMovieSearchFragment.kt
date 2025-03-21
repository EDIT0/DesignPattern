package com.my.mvistudymultimodule.feature.xml.moviesearch.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.my.mvistudymultimodule.core.base.BaseDataBindingFragment
import com.my.mvistudymultimodule.feature.xml.R
import com.my.mvistudymultimodule.feature.xml.databinding.FragmentXmlMovieSearchBinding

class XmlMovieSearchFragment : BaseDataBindingFragment<FragmentXmlMovieSearchBinding>(R.layout.fragment_xml_movie_search) {


    private var backPressCallback: OnBackPressedCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressCallback!!)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}