package com.example.recipes.adapters

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.databinding.FavouriteRecipeRowLayoutBinding
import com.example.recipes.ui.fragments.favourites.FavouriteRecipesFragmentDirections
import com.example.recipes.util.RecipesDiffUtil
import com.example.recipes.viewmodels.MainViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar


class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
): RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var multiSelection = false
    private lateinit var rootView: View
    private lateinit var mActionMode: ActionMode

    private var selectedRecipe = arrayListOf<FavouritesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

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
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentFavouritesEntity = favouritesRecipes[position]
        holder.bind(currentFavouritesEntity)

        // SingleClickListener
        holder.itemView.findViewById<ConstraintLayout>(R.id.favourite_RecipesRowLayout).setOnClickListener{

            if(multiSelection){
                applySelection(holder,currentFavouritesEntity)
            }else{
                val action =
                    FavouriteRecipesFragmentDirections.actionFavouriteRecipesFragmentToDetailsActivity(currentFavouritesEntity.result)
                holder.itemView.findNavController().navigate(action)
            }
        }

        // Long Click Listener
        holder.itemView.findViewById<ConstraintLayout>(R.id.favourite_RecipesRowLayout).setOnLongClickListener{

            if(!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder,currentFavouritesEntity)
                true
            }
            else{
                multiSelection = false
                false
            }
        }
    }


    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favourites_contextual_menu,menu)
        mActionMode = mode!!

        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_favourite_recipe_menu){
            selectedRecipe.forEach{
                mainViewModel.deleteFavouriteRecipes(it)
            }
            showSnackBar("${selectedRecipe.size} Recipe/s deleted")
            multiSelection = false
            selectedRecipe.clear()
            mode?.finish()
        }

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        multiSelection = false
        selectedRecipe.clear()

        myViewHolders.forEach{ it->
            changeRecipeStyle(it,R.color.cardBackgroundColor, R.color.strokeColor)
        }

        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun applyActionModeTitle(){
        when(selectedRecipe.size){
            0->{
                mActionMode.finish()
            }
            1->{
                mActionMode.title = "${selectedRecipe.size} item selected"
            }
            else -> {
                mActionMode.title = "${selectedRecipe.size} items selected"
            }
        }
    }
    private fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity,color)
    }

    fun setData(newFavouritesRecipes: List<FavouritesEntity>){

        val favouritesEntityDiffUtil = RecipesDiffUtil(favouritesRecipes,newFavouritesRecipes)

        val diffUtilResult = DiffUtil.calculateDiff(favouritesEntityDiffUtil)
        favouritesRecipes = newFavouritesRecipes

        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavouritesEntity){
        if(selectedRecipe.contains(currentRecipe)){
            selectedRecipe.remove(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        }else{
            selectedRecipe.add(currentRecipe)
            changeRecipeStyle(holder,R.color.cardBackgroundLightColor, R.color.colorPrimary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor:Int, strokeColor: Int){
        holder.itemView.findViewById<ConstraintLayout>(R.id.favourite_RecipesRowLayout).setBackgroundColor(
            ContextCompat.getColor(requireActivity,backgroundColor)
        )
        holder.itemView.findViewById<MaterialCardView>(R.id.favourite_row_cardView).strokeColor =
            ContextCompat.getColor(requireActivity,strokeColor)
    }

    private fun showSnackBar(message: String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}.show()
    }

    fun clearContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}