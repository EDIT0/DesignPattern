package com.example.mvvmexample1.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mvvmexample1.R
import com.example.mvvmexample1.databinding.ActivityMainBinding
import com.example.mvvmexample1.presentation.base.BaseDataBindingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val TAG = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}