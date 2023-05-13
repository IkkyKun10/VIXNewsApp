package com.riezki.vixnewsapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.riezki.vixnewsapp.MainActivity
import com.riezki.vixnewsapp.R
import com.riezki.vixnewsapp.databinding.FragmentDetailNewsBinding
import com.riezki.vixnewsapp.model.response.ArticlesItem
import com.riezki.vixnewsapp.ui.bookmarks.BookmarkViewModel
import com.riezki.vixnewsapp.utils.ViewModelFactory

class DetailNewsFragment : Fragment() {

    private val args: DetailNewsFragmentArgs by navArgs()

    private var _binding: FragmentDetailNewsBinding? = null
    private val binding get() = _binding!!

    private val bookmarkViewModel: BookmarkViewModel by viewModels {
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

        val article = args.articles
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        val navView = (activity as MainActivity).navView

        navView.visibility = View.GONE

        binding.fab.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}