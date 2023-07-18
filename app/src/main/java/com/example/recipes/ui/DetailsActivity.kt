package com.example.recipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.recipes.R
import com.example.recipes.adapters.PagerAdapter
import com.example.recipes.data.database.entities.FavouritesEntity
import com.example.recipes.ui.fragments.ingredients.IngredientsFragment
import com.example.recipes.ui.fragments.instructions.InstructionsFragment
import com.example.recipes.ui.fragments.overview.OverviewFragment
import com.example.recipes.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var detailsLayout : ConstraintLayout

    private var recipeSaved = false
    private var savedRecipeId = 0

    private val mainViewModel: MainViewModel by viewModels()

    private val args by navArgs<DetailsActivityArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        toolbar = findViewById<Toolbar>(R.id.toolbar)
        viewPager = findViewById<ViewPager2>(R.id.viewPager)
        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        detailsLayout = findViewById(R.id.detailsLayout)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle",args.result)

        val adapter = PagerAdapter(resultBundle,this,fragments)

        viewPager.adapter = adapter

        TabLayoutMediator(
            tabLayout, viewPager
        ){ tab, position -> tab.text = title[position] }.attach()

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favourites)
        checkSavedRecipes(menuItem!!)
        return true
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavouritesRecipes.observe(this) {favouritesEntity->
            try{
                for(savedRecipe in favouritesEntity){
                    if(savedRecipe.result.id == args.result.id){
                        changeMenuItemColor(menuItem,R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }else{
                        changeMenuItemColor(menuItem,R.color.white)
                    }
                }
            }catch (e: Exception){
                Log.d("DetailsActivity",e.message.toString())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
        }
        if(item.itemId == R.id.save_to_favourites && !recipeSaved){
            saveToFavourites(item)
        }
        if(item.itemId == R.id.save_to_favourites && recipeSaved){
            removeFromFavourites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavourites(item: MenuItem) {
        val favourite : FavouritesEntity = FavouritesEntity(
            0,
            args.result
        )

        mainViewModel.insertFavouriteRecipes(favourite)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved")

        recipeSaved = true
    }

    private fun removeFromFavourites(item: MenuItem){
        val favouritesEntity = FavouritesEntity(
            savedRecipeId,
            args.result
        )
        mainViewModel.deleteFavouriteRecipes(favouritesEntity)
        changeMenuItemColor(item,R.color.white)
        showSnackBar("Removed from favourites.")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(detailsLayout,message,Snackbar.LENGTH_SHORT).setAction("Ok"){}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, yellow: Int) {
        item.icon?.setTint(ContextCompat.getColor(this,yellow))
    }


}