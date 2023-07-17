package com.example.recipes.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.models.Result
import com.example.recipes.util.Constants.Companion.FAVOURITE_RECIPES_TABLE

@Entity(tableName = FAVOURITE_RECIPES_TABLE)
data class FavouritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    var result: Result
    )