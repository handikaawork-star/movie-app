package com.movieapp.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.movieapp.core.BuildConfig
import com.movieapp.core.R
import com.movieapp.core.databinding.ItemMovieBinding
import com.movieapp.core.domain.model.Movie

class MovieListAdapter(
    private val onItemClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvTitle.text = movie.title
            binding.tvReleaseDate.text = movie.releaseDate
            binding.tvRating.text = binding.root.context.getString(
                R.string.format_rating,
                movie.voteAverage
            )
            binding.tvOverview.text = movie.overview
            binding.ivPoster.load(BuildConfig.TMDB_IMAGE_BASE_URL + movie.posterPath) {
                placeholder(R.drawable.ic_movie_placeholder)
                error(R.drawable.ic_movie_placeholder)
            }
            binding.root.setOnClickListener { onItemClick(movie) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
        }
    }
}
