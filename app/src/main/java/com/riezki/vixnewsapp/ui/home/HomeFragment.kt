package com.riezki.vixnewsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.riezki.vixnewsapp.MainActivity
import com.riezki.vixnewsapp.adapter.NewsAdapter
import com.riezki.vixnewsapp.databinding.FragmentHomeBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Constant.QUERY_PAGE_SIZE
import com.riezki.vixnewsapp.utils.Resource

class HomeFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    lateinit var homeViewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

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
        homeViewModel = (activity as MainActivity).homeViewModel
        newsAdapter = NewsAdapter { item ->
            val action = HomeFragmentDirections.actionNavigationHomeToDetailNewsFragment(item)
            view.findNavController().navigate(action)
        }
        onRecyclerView()
        getHeadlineNews()
    }

    private fun showHeadlineItemFirst(articlesItem: List<ArticlesItem>) {
        binding.headlineItemFirst.imgHeadline.load(articlesItem[1].urlToImage)
        binding.headlineItemFirst.titleHeadlineTxt.text = articlesItem[1].title
        binding.headlineItemFirst.mediaPublishTxt.text = articlesItem[1].source?.name
        binding.headlineItemFirst.tanggalPublishTxt.text = articlesItem[1].publishedAt
    }

    private fun getHeadlineNews() {
        homeViewModel.headlineNewsData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data.let { newsResponse ->
                        newsResponse?.articles.let {itemList ->
                            showHeadlineItemFirst(itemList!!)
                        }

                        newsAdapter.submitList(newsResponse?.articles?.toList())
                        val totalPages = newsResponse?.totalResults!! / QUERY_PAGE_SIZE + 2
                        isLastPage = homeViewModel.headlinePageNumber == totalPages
                        if (isLastPage) {
                            binding.rvHeadline.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let {
                        Toast.makeText(activity, "An error occured: $it", Toast.LENGTH_LONG).show()
                        showErrorMessage(it.toString())
                    }
                }
            }
        }

        binding.btnTry.setOnClickListener {
            homeViewModel.getHeadlineNews("us")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                homeViewModel.getHeadlineNews("id")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        binding.errorMessage.visibility = View.GONE
        binding.btnTry.visibility = View.GONE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.errorMessage.visibility = View.VISIBLE
        binding.btnTry.visibility = View.VISIBLE
        binding.errorMessage.text = message
        isError = true
    }

    private fun onRecyclerView() {
        binding.rvHeadline.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
    }
}