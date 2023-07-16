package com.example.recipes.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.recipes.R
import com.example.recipes.models.Result
import com.example.recipes.util.Constants


class InstructionsFragment : Fragment() {

    private lateinit var webView: WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_instructions, container, false)

        webView = view.findViewById<WebView>(R.id.instruction_webView)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)



        webView.webViewClient = object : WebViewClient(){}


        val websiteURL: String = myBundle!!.sourceUrl
        view.findViewById<WebView>(R.id.instruction_webView).loadUrl(websiteURL)

        return view
    }
}