package com.example.mvcexample1.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvcexample1.R
import com.example.mvcexample1.controller.adapter.MovieAdapter
import com.example.mvcexample1.controller.adapter.SavedMoviesAdapter
import com.example.mvcexample1.databinding.ActivitySavedMoviesBinding
import com.example.mvcexample1.db.MovieDao
import com.example.mvcexample1.model.Movie
import com.example.mvcexample1.network.ApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SavedMoviesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedMoviesBinding

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var movieDao: MovieDao
    lateinit var movie: Movie
    private lateinit var savedMoviesAdapter: SavedMoviesAdapter

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setMovieModel()
        setSavedMovieRV()
        buttonClickListener()
        getSavedMovies()
    }

    private fun setMovieModel() {
        movie = Movie(apiService, movieDao, object : MainActivity.movieCallback {
            override suspend fun isLoading(value: Boolean) {
            }
        })
    }

    private fun setSavedMovieRV() {
        savedMoviesAdapter = SavedMoviesAdapter()

        binding.rvSavedMovies.apply {
            adapter = savedMoviesAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }

    private fun buttonClickListener() {
        savedMoviesAdapter.setOnItemClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                movie.deleteMovie(it)
                withContext(Dispatchers.Main) {
                    toast?.cancel()
                    toast = Toast.makeText(binding.root.context, "Delete ${it.title}", Toast.LENGTH_SHORT)
                    toast?.show()
                }
            }
        }
    }

    private fun getSavedMovies() {
        lifecycleScope.launch(Dispatchers.IO) {
            movie.getSavedMovies()
        }
        movie.savedMovieList.observe(this as LifecycleOwner) {
            savedMoviesAdapter.submitList(it)
        }
    }
}