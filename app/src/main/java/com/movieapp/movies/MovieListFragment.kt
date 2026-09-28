package com.movieapp.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.R
import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.presentation.adapter.MovieListAdapter
import com.movieapp.databinding.FragmentMovieListBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val category: MovieCategory by lazy {
        MovieCategory.valueOf(requireArguments().getString(ARG_CATEGORY)!!)
    }

    private val viewModel: MovieListViewModel by viewModel { parametersOf(category) }

    private val adapter = MovieListAdapter { movie ->
        findNavController().navigate(
            R.id.action_global_to_movieDetail,
            Bundle().apply { putInt("movieId", movie.id) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovies.adapter = adapter
        binding.swipeRefresh.setOnRefreshListener { viewModel.loadMovies() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> render(state) }
            }
        }
    }

    private fun showLoadingState(isLoading: Boolean, isError: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.tvError.isVisible = isError

    }

    private fun render(state: Resource<List<com.movieapp.core.domain.model.Movie>>) {
        binding.swipeRefresh.isRefreshing = false
        when (state) {
            is Resource.Loading -> showLoadingState(isLoading = true, isError = false)
            is Resource.Success -> {
               showLoadingState(isLoading = false, isError = false)
                adapter.submitList(state.data)
            }
            is Resource.Error -> {
                showLoadingState(isLoading = false, isError = true)
                binding.tvError.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        binding.rvMovies.adapter = null
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CATEGORY = "arg_category"

        fun newInstance(category: MovieCategory) = MovieListFragment().apply {
            arguments = Bundle().apply { putString(ARG_CATEGORY, category.name) }
        }
    }
}
