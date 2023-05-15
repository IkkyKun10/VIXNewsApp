package com.riezki.vixnewsapp.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.riezki.vixnewsapp.R
import com.riezki.vixnewsapp.databinding.FragmentDetailNewsBinding
import com.riezki.vixnewsapp.utils.ViewModelFactory

class DetailNewsFragment : Fragment() {

    private val args: DetailNewsFragmentArgs by navArgs()

    private var _binding: FragmentDetailNewsBinding? = null
    private val binding get() = _binding!!

    private val detailNewsViewModel: DetailNewsViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.newsEntity
        binding.webView.apply {
            webViewClient = WebViewClient()
            article?.url?.let { loadUrl(it) }
        }

        article?.let { detailNewsViewModel.setNewsData(it) }

        detailNewsViewModel.bookmarkStatus.observe(viewLifecycleOwner) {
            setBookmarkStatus(it)
        }

        binding.fab.setOnClickListener {
            article?.let { it1 -> detailNewsViewModel.changeBookmark(it1) }
        }
    }

    private fun setBookmarkStatus(state: Boolean) {
        if (state) {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite))
        } else {
            binding.fab.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite_border))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}