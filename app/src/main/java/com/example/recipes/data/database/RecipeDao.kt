package com.example.recipes.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity)

    @Query("Select * from recipes_table order by id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("Select * from favourite_recipes_table order by id ASC")
    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Delete
    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("Delete from favourite_recipes_table")
    suspend fun deleteAllFavouriteRecipe()


}