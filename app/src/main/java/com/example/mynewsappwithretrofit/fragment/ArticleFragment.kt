package com.example.mynewsappwithretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mynewsappwithretrofit.R
import com.example.mynewsappwithretrofit.model.Article
import com.example.mynewsappwithretrofit.ui.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class ArticleFragment : Fragment() {

    private lateinit var webView: WebView
    private lateinit var fab: FloatingActionButton
    private val viewModel: NewsViewModel by viewModels()  // نفس ViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_article, container, false)

        webView = view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.fab)

        val article = arguments?.getSerializable("article") as? Article

        // اجعل الروابط تُفتح داخل التطبيق بدلًا من المتصفح الخارجي
        webView.webViewClient = WebViewClient()

        // استرجع رابط المقال من الـ arguments
        val url = arguments?.getString("url")

        // تأكد أن الرابط غير فارغ ثم حمّله
        url?.let {
            webView.settings.javaScriptEnabled = true // تفعيل جافاسكريبت إذا الموقع يحتاج
            webView.loadUrl(it)
        }
        fab.setOnClickListener {
            article?.let { safeArticle ->
                viewModel.saveArticle(safeArticle)
                Toast.makeText(requireContext(), "تم حفظ المقال", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // للتأكد أن المستخدم إذا ضغط زر الرجوع يرجع خطوة داخل الـ WebView وليس يخرج من الـ Fragment
    override fun onResume() {
        super.onResume()
        webView.canGoBack()
    }
}