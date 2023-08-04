package com.example.recipes.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipes.models.FoodJoke
import com.example.recipes.util.Constants.Companion.FOOD_JOKE_TABLE


@Entity(tableName = FOOD_JOKE_TABLE)
data class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
){
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}
