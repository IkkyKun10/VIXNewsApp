package com.riezki.vixnewsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.riezki.vixnewsapp.adapter.ItemEverythingHeadlineAdapter
import com.riezki.vixnewsapp.adapter.ItemLoadingStateAdapter
import com.riezki.vixnewsapp.adapter.NewsAdapter
import com.riezki.vixnewsapp.data.local.entity.NewsEntity
import com.riezki.vixnewsapp.databinding.FragmentHomeBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Resource
import com.riezki.vixnewsapp.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var topHeadlineAdapter: ItemEverythingHeadlineAdapter

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topHeadlineAdapter = ItemEverythingHeadlineAdapter {item ->
            val newItem = NewsEntity(
                title = item.title.toString(),
                publishedAt = item.publishedAt.toString(),
                urlToImage = item.urlToImage,
                url = item.url,
                author = item.author
            )
            val action =
                HomeFragmentDirections.actionNavigationHomeToDetailNewsFragment(newItem)
            view.findNavController().navigate(action)
        }

        showRvHeadlineItemFirst()

        newsAdapter = NewsAdapter { item ->
            val newItem = NewsEntity(
                title = item.title.toString(),
                publishedAt = item.publishedAt.toString(),
                urlToImage = item.urlToImage,
                url = item.url,
                author = item.author
            )
            val action = HomeFragmentDirections.actionNavigationHomeToDetailNewsFragment(newItem)
            view.findNavController().navigate(action)
        }



        onRecyclerView(view)

        getHeadlineNews()

        getFirstHeadlineNews(view)

        homeViewModel.bookmarkStatus.observe(viewLifecycleOwner) {

        }

        newsAdapter.setOnClickItemListener(object : NewsAdapter.ItemOnClickListener {
            override fun onBookmarkClick(articlesItem: ArticlesItem?) {
                val newsEntity = NewsEntity(
                    title = articlesItem?.title.toString(),
                    publishedAt = articlesItem?.publishedAt.toString(),
                    urlToImage = articlesItem?.urlToImage,
                    url = articlesItem?.url,
                    author = articlesItem?.author
                )
                homeViewModel.changeBookmark(newsEntity)
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showRvHeadlineItemFirst() {
        binding.rvEverythingHeadline.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
            adapter = topHeadlineAdapter
            setHasFixedSize(true)
            setPadding(12, 12, 12, 12)
        }
    }

    private fun getHeadlineNews() {
        showProgressBar()
        homeViewModel.headlineNewsData.observe(viewLifecycleOwner) { response ->
            hideProgressBar()
            newsAdapter.submitData(lifecycle, response)
            //showHeadlineItemFirst(newsAdapter.snapshot().items)
            val list = newsAdapter.snapshot().items
            list.map {
                val newsForCheckingBookmark = NewsEntity(
                    title = it.title.toString(),
                    publishedAt = it.publishedAt.toString(),
                    urlToImage = it.urlToImage,
                    url = it.url,
                    author = it.author
                )
                homeViewModel.setCheckingData(newsForCheckingBookmark)
            }
        }
    }

    private fun getFirstHeadlineNews(view: View) {
        homeViewModel.getFirstHeadlineNews("us").observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    hideProgressBar()
                    topHeadlineAdapter.submitList(response.data)
                }

                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, "404 Not Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

//    private fun hideErrorMessage() {
//        binding.errorMessage.visibility = View.GONE
//        binding.btnTry.visibility = View.GONE
//        isError = false
//    }
//
//    private fun showErrorMessage(message: String) {
//        binding.errorMessage.visibility = View.VISIBLE
//        binding.btnTry.visibility = View.VISIBLE
//        binding.errorMessage.text = message
//        isError = true
//    }

    private fun onRecyclerView(view: View) {
        binding.rvHeadline.apply {
            adapter = newsAdapter.withLoadStateFooter(
                footer = ItemLoadingStateAdapter {
                    newsAdapter.retry()
                }
            )
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }
}