package com.example.mvvmarchitecturestudy2.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mvvmarchitecturestudy2.R
import com.example.mvvmarchitecturestudy2.databinding.FragmentInfoBinding
import com.example.mvvmarchitecturestudy2.presentation.ui.activity.MovieInfoActivity
import com.example.mvvmarchitecturestudy2.presentation.viewmodel.MovieInfoViewModel

class InfoFragment : Fragment() {

    private val TAG = InfoFragment::class.simpleName

    private lateinit var fragmentInfoBinding : FragmentInfoBinding

    lateinit var movieInfoViewModel: MovieInfoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView() ${this.hashCode()}")

        fragmentInfoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_info, container, false)
        return fragmentInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(TAG, "onViewCreated()")

        movieInfoViewModel = (activity as MovieInfoActivity).movieInfoViewModel

        observeListener()
    }

    private fun observeListener() {
        movieInfoViewModel.movieDetailResponse.observe(viewLifecycleOwner, {
            Log.i(TAG, "value : ${it}")

            fragmentInfoBinding.movieDetail = it

        })
    }
}