package com.example.mynewsappwithretrofit.fragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewsappwithretrofit.R
import com.example.mynewsappwithretrofit.adapter.NewsAdapter
import com.example.mynewsappwithretrofit.databinding.FragmentSavedNewsBinding
import com.example.mynewsappwithretrofit.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class SavedNewsFragment : Fragment() {

    private var _binding: FragmentSavedNewsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // مراقبة المقالات المحفوظة وعرضها
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getSavedArticles().collect { articles ->
                newsAdapter.submitData(lifecycle, PagingData.from(articles))
            }
        }

        // Swipe to delete مع Undo
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val article = newsAdapter.snapshot()[position]
                article?.let {
                    viewModel.deleteArticle(it)

                    Snackbar.make(requireView(), "تم حذف المقال", Snackbar.LENGTH_LONG).apply {
                        setAction("تراجع") {
                            viewModel.saveArticle(article)

                            newsAdapter.notifyItemChanged(position)
                        }
                        show()
                    }
                }
            }
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)!!

                val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight

                if (dX > 0) { // السحب لليمين
                    icon.setBounds(
                        itemView.left + iconMargin,
                        iconTop,
                        itemView.left + iconMargin + icon.intrinsicWidth,
                        iconBottom
                    )
                } else if (dX < 0) { // السحب لليسار
                    icon.setBounds(
                        itemView.right - iconMargin - icon.intrinsicWidth,
                        iconTop,
                        itemView.right - iconMargin,
                        iconBottom
                    )
                } else {
                    icon.setBounds(0, 0, 0, 0)
                }

                icon.draw(c)

                // استدعاء السوبر بدون رسم الخلفية (background)
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

        }


        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvSavedNews)
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { article ->
            val bundle = Bundle().apply { putSerializable("article", article) }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
