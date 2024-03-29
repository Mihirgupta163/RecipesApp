package com.example.recipes.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.adapters.FavouriteRecipesAdapter
import com.example.recipes.data.database.entities.FavouritesEntity

class FavouriteRecipeBinding {

    companion object{

        @JvmStatic
        @BindingAdapter("viewVisibility","setData", requireAll = false)
        fun setDataAndViewVisibility(view: View, favouritesEntity: List<FavouritesEntity>?, mAdapter: FavouriteRecipesAdapter?){

            if(favouritesEntity.isNullOrEmpty()){
                when(view){
                    is ImageView->{
                        view.visibility = View.VISIBLE
                    }
                    is TextView->{
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }else{
                when(view){
                    is ImageView->{
                        view.visibility = View.INVISIBLE
                    }
                    is TextView->{
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favouritesEntity)
                    }
                }
            }
        }

    }
}