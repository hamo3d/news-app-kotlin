package com.example.mynewsappwithretrofit.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynewsappwithretrofit.adapter.NewsAdapter
import com.example.mynewsappwithretrofit.databinding.FragmentBreakingNewsBinding
import com.example.mynewsappwithretrofit.ui.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BreakingNewsFragment : Fragment() {

    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // تهيئة Adapter مع حدث الضغط على المقال
        newsAdapter = NewsAdapter { article ->
            val bundle = Bundle().apply {
                putString("url", article.url)
                putSerializable("article", article)
            }
            findNavController().navigate(
                com.example.mynewsappwithretrofit.R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        // إعداد RecyclerView مرة واحدة
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // تحميل البيانات
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getBreakingNews("us").collect { pagingData ->
                    newsAdapter.submitData(pagingData)
                }
            }
        }

        // إظهار/إخفاء الـ ProgressBar حسب حالة التحميل
        newsAdapter.addLoadStateListener { loadState ->
            binding.paginationProgressBar.isVisible = loadState.source.refresh is LoadState.Loading

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "Error: ${it.error.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
