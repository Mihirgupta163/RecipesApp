package com.example.recipes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.databinding.FavouriteRecipeRowLayoutBinding
import com.example.recipes.util.RecipesDiffUtil


class FavouriteRecipesAdapter: RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>() {

    private var favouritesRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(private val binding: FavouriteRecipeRowLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(favouritesEntity: FavouritesEntity){
            binding.favouritesEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavouriteRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return favouritesRecipes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFavouritesEntity = favouritesRecipes[position]
        holder.bind(currentFavouritesEntity)
    }

    fun setData(newFavouritesRecipes: List<FavouritesEntity>){

        val favouritesEntityDiffUtil = RecipesDiffUtil(favouritesRecipes,newFavouritesRecipes)

        val diffUtilResult = DiffUtil.calculateDiff(favouritesEntityDiffUtil)
        favouritesRecipes = newFavouritesRecipes

        diffUtilResult.dispatchUpdatesTo(this)
    }
}