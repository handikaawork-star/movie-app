package com.movieapp.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.movieapp.R
import com.movieapp.core.domain.model.Resource
import com.movieapp.core.presentation.adapter.MovieListAdapter
import com.movieapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()

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
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onQueryChanged(newText.orEmpty())
                return true
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state -> render(state) }
            }
        }
    }

    private fun render(state: Resource<List<com.movieapp.core.domain.model.Movie>>) {
        when (state) {
            is Resource.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
                binding.tvEmpty.visibility = View.GONE
            }
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                adapter.submitList(state.data)
                if (state.data.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = if (viewModel.query.value.isBlank()) {
                        getString(R.string.search_placeholder)
                    } else {
                        getString(R.string.search_no_result)
                    }
                } else {
                    binding.tvEmpty.visibility = View.GONE
                }
            }
            is Resource.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                binding.tvEmpty.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        binding.searchView.setOnQueryTextListener(null)
        binding.rvSearchResults.adapter = null
        super.onDestroyView()
        _binding = null
    }
}
