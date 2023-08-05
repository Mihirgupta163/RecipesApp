package com.example.recipes.bindingadapters

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.models.FoodJoke
import com.example.recipes.util.NetworkResult

class FoodJokeBinding {

    companion object {

        @BindingAdapter("readApiResponse","readDatabase", requireAll = false)
        fun setCardAndProgressVisibility(view : View, apiResponse: NetworkResult<FoodJoke>?, database: List<FoodJokeEntity>?){
            when(apiResponse){
                is NetworkResult.Loading ->{

                }

                else -> {}
            }
        }

    }
}