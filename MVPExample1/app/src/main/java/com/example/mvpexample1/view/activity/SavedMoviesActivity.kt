package com.example.mvpexample1.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvpexample1.databinding.ActivitySavedMoviesBinding
import com.example.mvpexample1.model.db.MovieDao
import com.example.mvpexample1.model.network.ApiService
import com.example.mvpexample1.model.usecase.DeleteMovieUseCase
import com.example.mvpexample1.model.usecase.GetAllSavedMoviesUseCase
import com.example.mvpexample1.presenter.MainPresenter
import com.example.mvpexample1.presenter.SavedMoviesContract
import com.example.mvpexample1.presenter.SavedMoviesPresenter
import com.example.mvpexample1.view.adapter.SavedMoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SavedMoviesActivity : AppCompatActivity(), SavedMoviesContract.View {

    private lateinit var binding: ActivitySavedMoviesBinding
    private lateinit var savedMoviesPresenter: SavedMoviesPresenter

    @Inject
    lateinit var getAllSavedMoviesUseCase: GetAllSavedMoviesUseCase
    @Inject
    lateinit var deleteMovieUseCase: DeleteMovieUseCase

    private lateinit var savedMoviesAdapter: SavedMoviesAdapter

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPresenter()
        setSavedMovieRV()
        buttonClickListener()
        getSavedMovies()
    }

    override suspend fun showProgress(value: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showToast(str: String) {
        toast?.cancel()
        toast = Toast.makeText(binding.root.context, str, Toast.LENGTH_SHORT)
        toast?.show()
    }

    private fun setPresenter() {
        savedMoviesPresenter = SavedMoviesPresenter(
            this,
            getAllSavedMoviesUseCase,
            deleteMovieUseCase
        )
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
                savedMoviesPresenter.deleteMovie(it)
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
            savedMoviesPresenter.getSavedMovies()
        }
        savedMoviesPresenter.savedMovieList.observe(this as LifecycleOwner) {
            savedMoviesAdapter.submitList(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        savedMoviesPresenter.releaseView()
    }
}