package com.my.mvistudymultimodule.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

open class BaseDataBindingFragment<T: ViewDataBinding>(@LayoutRes val layoutResId: Int) : Fragment() {

    // Binding
    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingDataBinding(inflater = inflater, container = container)
        return binding.root
    }

    /**
     * 데이터 바인딩 셋팅
     *
     * @param inflater
     * @param container
     */
    private fun settingDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
