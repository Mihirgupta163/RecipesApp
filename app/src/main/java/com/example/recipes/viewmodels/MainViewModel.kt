package com.example.recipes.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.Repository
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.data.database.entities.FoodJokeEntity
import com.example.recipes.data.database.entities.RecipesEntity
import com.example.recipes.models.FoodJoke
import com.example.recipes.models.FoodRecipe
import com.example.recipes.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    myApplication: Application,
    private val repository: Repository
) : AndroidViewModel(myApplication) {

    /** Room Database */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavouritesRecipes: LiveData<List<FavouritesEntity>> = repository.local.readFavouriteRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    /** Retrofit Call */
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipeResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse : MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()


    fun searchRecipes(searchQuery: Map<String,String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }


    fun getRecipe(queries: Map<String,String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun getFoodJoke(apiKey: String)= viewModelScope.launch {
        getFoodJokeSafeCall(apiKey)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>){
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try{
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipeResponse(response)

                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }

            }catch (e: Exception){
                recipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        }
        else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection",)
        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>){
        searchRecipeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try{
                val response = repository.remote.searchRecipes(searchQuery)
                searchRecipeResponse.value = handleFoodRecipeResponse(response)

            }catch (e: Exception){
                searchRecipeResponse.value = NetworkResult.Error("Recipes not found")
            }
        }
        else{
            searchRecipeResponse.value = NetworkResult.Error("No Internet Connection",)
        }
    }

    private suspend fun getFoodJokeSafeCall(apiKey: String){
        foodJokeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try{
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if(foodJoke!=null)
                    offlineCacheFoodJoke(foodJoke)
            }catch (e:Exception){
                foodJokeResponse.value = NetworkResult.Error("Food Joke not found")
            }
        }
        else{
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>?{
        when{
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited")
            }
            response.isSuccessful() -> {
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    fun insertFavouriteRecipes(favouritesEntity: FavouritesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavouriteRecipes(favouritesEntity)
        }
    }

    fun deleteFavouriteRecipes(favouritesEntity: FavouritesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavouriteRecipe(favouritesEntity)
        }
    }

    fun deleteAllFavouriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavouriteRecipes()
        }

    private fun insertRecipes(recipesEntity: RecipesEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }
    }

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        viewModelScope.launch (Dispatchers.IO){
            repository.local.insertFoodJoke(foodJokeEntity)
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes Not Found")
            }

            response.isSuccessful() -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->  true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->  true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ->  true
            else -> false
        }

    }
}