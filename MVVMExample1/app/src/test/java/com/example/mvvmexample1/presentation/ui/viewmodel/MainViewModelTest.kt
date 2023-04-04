package com.example.mvvmexample1.presentation.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.mvvmexample1.presentation.ui.activity.MainActivity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

class MainViewModelTest {
    private var activity: MainActivity? = null

    @Inject
    lateinit var mainViewModelFactory : MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel

    @get:Rule
    var mActivityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        mActivityRule.getScenario().onActivity { activity ->
            this.activity = activity
            mainViewModel = ViewModelProvider(activity, mainViewModelFactory).get(MainViewModel::class.java)
        }
    }

    @Test
    fun searchMovies() {
        mainViewModel.searchMovies()
    }
}