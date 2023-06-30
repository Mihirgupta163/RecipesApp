package com.example.recipes.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.cooltechworks.views.shimmer.ShimmerRecyclerView
import com.example.recipes.viewmodels.MainViewModel
import com.example.recipes.R
import com.example.recipes.adapters.RecipesAdapter
import com.example.recipes.databinding.FragmentRecipesBinding
import com.example.recipes.util.Constants.Companion.API_KEY
import com.example.recipes.util.NetworkResult
import com.example.recipes.util.observeOnce
import com.example.recipes.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private val args by navArgs<RecipesFragmentArgs>()

    private var _binding : FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesViewModel: RecipesViewModel
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { RecipesAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding =  FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        setUpRecyclerView()
        readDatabase()

        binding.recipesFab.setOnClickListener{
            findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
        }

        return binding.root
    }

    private fun setUpRecyclerView(){
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()

    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) {database->
                Log.d("RecipesFragment",args.backFromBottomSheet.toString())
                if(database.isNotEmpty() && !args.backFromBottomSheet){
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }else{
                    requestAPIData()
                }
            }
        }
    }

    private fun requestAPIData(){
        mainViewModel.getRecipe(recipesViewModel.applyQueries())

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
                    loadDataFromCache()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadDataFromCache(){
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner){db->
                mAdapter.setData(db[0].foodRecipe)
            }
        }
    }

    private fun showShimmerEffect(){
        binding.recyclerview.showShimmerAdapter()
    }

    private fun hideShimmerEffect(){
        binding.recyclerview.hideShimmerAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}