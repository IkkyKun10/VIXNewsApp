package com.riezki.vixnewsapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.riezki.vixnewsapp.adapter.ItemLoadingStateAdapter
import com.riezki.vixnewsapp.adapter.NewsAdapter
import com.riezki.vixnewsapp.databinding.FragmentHomeBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.utils.Constant.QUERY_PAGE_SIZE
import com.riezki.vixnewsapp.utils.Resource
import com.riezki.vixnewsapp.utils.ViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var newsAdapter: NewsAdapter
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    //var isError = false
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

        newsAdapter = NewsAdapter { item ->
            val action = HomeFragmentDirections.actionNavigationHomeToDetailNewsFragment(item)
            view.findNavController().navigate(action)
        }

        onRecyclerView(view)

        getHeadlineNews()

        getFirstHeadlineNews(view)
    }

    private fun showHeadlineItemFirst(articlesItem: List<ArticlesItem>?) {
        binding.headlineItemFirst.imgHeadline.load(articlesItem?.get(1)?.urlToImage)
        binding.headlineItemFirst.titleHeadlineTxt.text = articlesItem?.get(1)?.title
        binding.headlineItemFirst.mediaPublishTxt.text = articlesItem?.get(1)?.source?.name
        binding.headlineItemFirst.tanggalPublishTxt.text = articlesItem?.get(1)?.publishedAt
    }

    private fun getHeadlineNews() {
        showProgressBar()
        homeViewModel.headlineNewsData.observe(viewLifecycleOwner) { response ->
            hideProgressBar()
            newsAdapter.submitData(lifecycle, response)
        }
    }

    private fun getFirstHeadlineNews(view: View) {
        lifecycleScope.launch {
            homeViewModel.getFirstHeadlineNews("us").observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        showHeadlineItemFirst(response.data)
                        binding.headlineItemFirst.imgHeadline.setOnClickListener {
                            val action = HomeFragmentDirections
                                .actionNavigationHomeToDetailNewsFragment(response.data?.get(1)!!)
                            view.findNavController().navigate(action)
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(context, "404 Not Found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

            val isNoErrors = false //!isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                //homeViewModel.getHeadlineNews("us")
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
//            addOnScrollListener(this@HomeFragment.scrollListener)
            setHasFixedSize(true)
        }
    }
}