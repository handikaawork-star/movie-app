package com.movieapp.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.movieapp.R
import com.movieapp.core.BuildConfig
import com.movieapp.core.domain.model.MovieDetail
import com.movieapp.core.domain.model.Resource
import com.movieapp.databinding.FragmentMovieDetailBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val movieId: Int by lazy { requireArguments().getInt(ARG_MOVIE_ID) }

    private val viewModel: MovieDetailViewModel by viewModel { parametersOf(movieId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFavorite.setOnClickListener { viewModel.toggleFavorite() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.detailState.collect { render(it) } }
                launch { viewModel.isFavorite.collect { renderFavorite(it) } }
            }
        }
    }

    private fun render(state: Resource<MovieDetail>) {
        binding.progressBar.visibility = View.GONE
        binding.tvError.visibility = View.GONE
        when (state) {
            is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Resource.Error -> {
                binding.tvError.visibility = View.VISIBLE
                binding.tvError.text = state.message
            }
            is Resource.Success -> bindDetail(state.data)
        }
    }

    private fun bindDetail(detail: MovieDetail) {
        binding.tvTitle.text = detail.title
        binding.tvOverview.text = detail.overview
        binding.tvMeta.text = getString(
            R.string.format_detail_meta,
            detail.releaseDate,
            detail.runtime,
            detail.voteAverage
        )
        binding.tvGenres.text = detail.genres.joinToString(", ")
        binding.ivPoster.load(BuildConfig.TMDB_IMAGE_BASE_URL + detail.posterPath)
        binding.ivBackdrop.load(BuildConfig.TMDB_IMAGE_BASE_URL + detail.backdropPath)
    }

    private fun renderFavorite(isFavorite: Boolean) {
        binding.btnFavorite.setImageResource(
            if (isFavorite) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MOVIE_ID = "movieId"
    }
}
