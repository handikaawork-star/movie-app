package com.movieapp.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.movieapp.R
import com.movieapp.core.domain.model.MovieCategory
import com.movieapp.databinding.FragmentMoviesPagerBinding

class MoviesPagerFragment : Fragment(R.layout.fragment_movies_pager) {

    private var _binding: FragmentMoviesPagerBinding? = null
    private val binding get() = _binding!!

    private var tabLayoutMediator: TabLayoutMediator? = null

    private val categories = listOf(
        MovieCategory.NOW_PLAYING,
        MovieCategory.POPULAR,
        MovieCategory.TOP_RATED
    )

    private val titles = listOf(
        R.string.tab_now_playing,
        R.string.tab_popular,
        R.string.tab_top_rated
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = object : FragmentStateAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle
        ) {
            override fun getItemCount() = categories.size
            override fun createFragment(position: Int) =
                MovieListFragment.newInstance(categories[position])
        }

        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(titles[position])
        }.also { it.attach() }
    }

    override fun onDestroyView() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
        binding.viewPager.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
