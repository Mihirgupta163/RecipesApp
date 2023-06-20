package com.example.recipes.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.models.FoodRecipe
import com.example.recipes.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
data class RecipesEntity (val foodRecipe: FoodRecipe){

    @PrimaryKey(autoGenerate = false)
    var id:Int = 0

}