package com.example.recipes.ui.fragments.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.adapters.FavouriteRecipesAdapter
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.databinding.FavouriteRecipeRowLayoutBinding
import com.example.recipes.databinding.FragmentFavouriteRecipesBinding
import com.example.recipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavouriteRecipesBinding? = null
    private val binding get() = _binding!!


    private val mainViewModel : MainViewModel by  viewModels()
    private val mAdapter: FavouriteRecipesAdapter by lazy { FavouriteRecipesAdapter(requireActivity(),mainViewModel) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpMenu()
    }
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
        mAdapter.clearContextualActionMode()
    }

    private fun setUpMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.favourite_recipes_menu,menu)
                val delete = menu.findItem(R.id.deleteAll_favourite_recipes_menu)


            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if(menuItem.itemId == R.id.deleteAll_favourite_recipes_menu){
                    mainViewModel.deleteAllFavouriteRecipes()
                    showSnackBar("All favourites recipes deleted.")
                }
                return true
            }
        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}.show()
    }
}