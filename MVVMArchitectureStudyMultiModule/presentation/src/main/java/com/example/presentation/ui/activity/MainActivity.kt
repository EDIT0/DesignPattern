package com.example.presentation.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.presentation.databinding.ActivityMainBinding
import com.example.presentation.adapter.MovieAdapter
import com.example.presentation.adapter.SearchMovieAdapter
import com.example.presentation.viewmodel.MainViewModel
import com.example.presentation.viewmodel.MainViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.view.MenuItem
import com.example.presentation.R
import com.example.presentation.adapter.SavedMoviesAdapter
import com.example.presentation.ui.fragment.MovieListFragment
import com.example.presentation.ui.fragment.SavedMoviesFragment
import com.example.presentation.ui.fragment.SearchMovieFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MYTAG"
    }

    lateinit var binding : ActivityMainBinding

    lateinit var mainViewModel : MainViewModel
    @Inject
    lateinit var mainViewModelFactory : MainViewModelFactory
    @Inject
    lateinit var movieAdapter: MovieAdapter
    @Inject
    lateinit var searchMovieAdapter: SearchMovieAdapter
    @Inject
    lateinit var savedMoviesAdapter: SavedMoviesAdapter


    private var fragmentManager: FragmentManager? = null
    private var movieListFragment : MovieListFragment? = null
    private var searchMovieFragment: SearchMovieFragment? = null
    private var savedMovieFragment: SavedMoviesFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)

        fragmentManager = supportFragmentManager

        movieListFragment = MovieListFragment()
        fragmentManager?.beginTransaction()?.replace(binding.framelayout.id, movieListFragment!!)?.commit()

        binding.bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId){
                    R.id.movieListFragment -> {
                        if (movieListFragment == null) {
                            movieListFragment = MovieListFragment()
                            fragmentManager!!.beginTransaction().add(binding.framelayout.id, movieListFragment!!).commit()
                        }

                        if (movieListFragment != null) fragmentManager!!.beginTransaction().show(movieListFragment!!).commit()
                        if (searchMovieFragment != null) fragmentManager!!.beginTransaction().hide(searchMovieFragment!!).commit()
                        if (savedMovieFragment != null) fragmentManager!!.beginTransaction().hide(savedMovieFragment!!).commit()
                    }
                    R.id.searchMovieFragment -> {
                        if (searchMovieFragment == null) {
                            searchMovieFragment = SearchMovieFragment()
                            fragmentManager!!.beginTransaction().add(binding.framelayout.id, searchMovieFragment!!).commit()
                        }

                        if (movieListFragment != null) fragmentManager!!.beginTransaction().hide(movieListFragment!!).commit()
                        if (searchMovieFragment != null) fragmentManager!!.beginTransaction().show(searchMovieFragment!!).commit()
                        if (savedMovieFragment != null) fragmentManager!!.beginTransaction().hide(savedMovieFragment!!).commit()
                    }
                    R.id.savedMoviesFragment -> {
                        if (savedMovieFragment == null) {
                            savedMovieFragment = SavedMoviesFragment()
                            fragmentManager!!.beginTransaction().add(binding.framelayout.id, savedMovieFragment!!).commit()
                        }

                        if (movieListFragment != null) fragmentManager!!.beginTransaction().hide(movieListFragment!!).commit()
                        if (searchMovieFragment != null) fragmentManager!!.beginTransaction().hide(searchMovieFragment!!).commit()
                        if (savedMovieFragment != null) fragmentManager!!.beginTransaction().show(savedMovieFragment!!).commit()
                    }
                }

                return true
            }
        })

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        binding.bottomNavigation.setupWithNavController(navController)



//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        // KeepStateNavigator navController에 추가
//        val navigator = KeepStateNavigator(this, navHostFragment.childFragmentManager, R.id.fragment)
//        navController.navigatorProvider.addNavigator(navigator)
//
//        navController.setGraph(R.navigation.nav_bottom_menu_graph)
//
//        // 바텀 네비게이션 뷰와 navController 연결
//        binding.bottomNavigation.setupWithNavController(navController)

    }
}