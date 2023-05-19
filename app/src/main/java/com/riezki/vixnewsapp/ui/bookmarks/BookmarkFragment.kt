package com.riezki.vixnewsapp.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.riezki.vixnewsapp.MainActivity
import com.riezki.vixnewsapp.R
import com.riezki.vixnewsapp.adapter.BookmarkAdapter
import com.riezki.vixnewsapp.adapter.NewsAdapter
import com.riezki.vixnewsapp.databinding.FragmentBookmarkBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.ViewModelFactory

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val bookmarkViewModel: BookmarkViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var bookmarkAdapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkAdapter = BookmarkAdapter { data ->
            val action = BookmarkFragmentDirections.actionNavigationBookmarkToDetailNewsFragment(data)
            view.findNavController().navigate(action)
        }
        showRecyler()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = bookmarkAdapter.currentList[position]
                bookmarkViewModel.deleteNews(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        bookmarkViewModel.saveNews(article)
                    }
                    show()
                }
            }
        }

        bookmarkViewModel.getBookmarkedNews().observe(viewLifecycleOwner) {response ->
            bookmarkAdapter.submitList(response)
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvHeadline)
    }

    private fun showRecyler() {
        binding.rvHeadline.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookmarkAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}