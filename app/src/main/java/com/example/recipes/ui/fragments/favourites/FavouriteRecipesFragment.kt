package com.example.recipes.ui.fragments.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.adapters.FavouriteRecipesAdapter
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.databinding.FavouriteRecipeRowLayoutBinding
import com.example.recipes.databinding.FragmentFavouriteRecipesBinding
import com.example.recipes.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity()) }
    private val mainViewModel : MainViewModel by  viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteRecipesBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = this

        binding.mainViewModel = this.mainViewModel
        binding.mAdapter = this.mAdapter

        setUpRecyclerView()

        return binding.root
    }

    private fun setUpRecyclerView(){
        binding.favouriteRecipesRecyclerView.adapter = mAdapter
        binding.favouriteRecipesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}