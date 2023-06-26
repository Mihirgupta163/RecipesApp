package com.example.recipes.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.recipes.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.recipes.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.recipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.recipes.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.recipes.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferencesKey{
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)

        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
    }

    private val Context.dataStore by preferencesDataStore(
        name = PREFERENCES_NAME
    )

    fun saveMealAndDietType(mealType:String, mealTypeId:Int, dietType: String, dietTypeId: Int){

    }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId:Int,

    val selectedDietType:String,
    val selectedDietTypeId: Int
)