package com.example.recipes.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.recipes.MainViewModel
import com.example.recipes.R
import com.example.recipes.adapters.RecipesAdapter
import com.example.recipes.util.Constants.Companion.API_KEY
import com.example.recipes.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View
    private lateinit var recyclerView: ShimmerRecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_recipes, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        recyclerView = mView.findViewById(R.id.recyclerview)

        setUpRecyclerView()
        requestAPIData()

        return mView
    }

    private fun requestAPIData(){
        mainViewModel.getRecipe(applyQueries())

        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun applyQueries():HashMap<String,String>{
        val queries: HashMap<String, String> = HashMap()

        queries["number"] = "50"
        queries["apiKey"] = API_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["RecipeInformation"] = "true"
        queries["fillIngredient"] = "true"

        return queries
    }
    private fun setUpRecyclerView(){
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()

    }

    private fun showShimmerEffect(){
        mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).showShimmerAdapter()
    }

    private fun hideShimmerEffect(){
        mView.findViewById<ShimmerRecyclerView>(R.id.recyclerview).hideShimmerAdapter()
    }

}