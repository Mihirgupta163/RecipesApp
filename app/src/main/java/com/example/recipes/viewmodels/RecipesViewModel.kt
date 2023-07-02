package com.example.recipes.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.DataStoreRepository
import com.example.recipes.util.Constants.Companion.API_KEY
import com.example.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.recipes.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.recipes.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.recipes.util.Constants.Companion.QUERY_API_KEY
import com.example.recipes.util.Constants.Companion.QUERY_DIET
import com.example.recipes.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.recipes.util.Constants.Companion.QUERY_NUMBER
import com.example.recipes.util.Constants.Companion.QUERY_SEARCH
import com.example.recipes.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStore: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStore.readMealAndDietType
    val readBackOnline = dataStore.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType:String, mealTypeId:Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveMealAndDietType(mealType,mealTypeId, dietType, dietTypeId)
        }

    fun saveBackOnline(backOnline:Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveBackOnline(backOnline)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): Map<String, String>{
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus(){
        if(!networkStatus){
            Toast.makeText(getApplication(),"No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        }else if(networkStatus){
            if(backOnline){
                Toast.makeText(getApplication(),"We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
    fun applyOrderFood(){
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
    }
}