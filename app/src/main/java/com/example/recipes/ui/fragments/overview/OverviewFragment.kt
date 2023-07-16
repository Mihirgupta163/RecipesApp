package com.example.recipes.ui.fragments.overview

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.recipes.R
import com.example.recipes.databinding.FragmentOverviewBinding
import com.example.recipes.models.Result
import com.example.recipes.util.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {
    private lateinit var binding: FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args?.getParcelable(RECIPE_RESULT_KEY,Result::class.java)
        }else{
            args?.getParcelable(RECIPE_RESULT_KEY)
        }

        binding.mainImageView.load(myBundle?.image)
        binding.titleTextview.text = myBundle?.title
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()
        binding.timeTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary?.let {
            val summary = Jsoup.parse(it).text()
            binding.summaryTextview.text=summary
        }

        if(myBundle?.vegetarian == true){
            binding.vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.vegetarianTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.vegan == true){
            binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.veryHealthy == true){
            binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.cheap == true){
            binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.glutenFree == true){
            binding.glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if(myBundle?.dairyFree == true){
            binding.dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.dairyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        return binding.root
    }
}