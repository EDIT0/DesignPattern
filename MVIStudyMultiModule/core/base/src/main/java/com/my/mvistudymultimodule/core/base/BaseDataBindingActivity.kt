package com.my.mvistudymultimodule.core.base

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import javax.inject.Inject

open class BaseDataBindingActivity<T : ViewDataBinding>(@LayoutRes val layoutResId: Int): AppCompatActivity() {
    @Inject
    open lateinit var activityNavigator: ActivityNavigator

    private var _binding: T? = null
    open val binding get() = _binding!!

    // Activity
    open lateinit var activity: Activity

    open val activityScope: LifecycleCoroutineScope = lifecycleScope

    // Toast
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingDataBinding()
    }
    /**
     * 데이터 바인딩 셋팅
     */
    private fun settingDataBinding() {
        _binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this
        activity = this
    }

    /**
     * 공통 토스트 메시지
     *
     * @param message 메시지 내용
     */
    open fun showToast(message: String?) {
//        CommonToast.makeToast(binding.root, message?:"")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}