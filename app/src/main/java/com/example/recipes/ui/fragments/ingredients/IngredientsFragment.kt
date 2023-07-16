package com.example.recipes.ui.fragments.ingredients

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.R
import com.example.recipes.adapters.IngredientsAdapter
import com.example.recipes.models.ExtendedIngredient
import com.example.recipes.models.Result
import com.example.recipes.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView =  inflater.inflate(R.layout.fragment_ingredients, container, false)

        recyclerView = mView.findViewById<RecyclerView>(R.id.ingredient_recyclerView)



        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)


        setUpRecyclerView()

        myBundle?.extendedIngredients?.let{
            ingredientsAdapter.setData(it)
        }

        return mView
    }


    private fun setUpRecyclerView(){
        recyclerView.adapter = ingredientsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}