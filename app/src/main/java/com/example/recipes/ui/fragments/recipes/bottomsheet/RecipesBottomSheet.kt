package com.example.recipes.ui.fragments.recipes.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.recipes.R
import com.example.recipes.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.recipes.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.recipes.viewmodels.RecipesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale


class RecipesBottomSheet : BottomSheetDialogFragment() {
    private lateinit var recipesViewModel: RecipesViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val mView = inflater.inflate(R.layout.recipes_bottom_sheet, container, false)

        recipesViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner) { value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType
            updateChip(
                value.selectedMealTypeId,
                mView.findViewById<ChipGroup>(R.id.mealType_chipGroup)
            )
            updateChip(
                value.selectedDietTypeId,
                mView.findViewById<ChipGroup>(R.id.dietType_chipGroup)
            )

        }

        mView.findViewById<ChipGroup>(R.id.mealType_chipGroup)
            .setOnCheckedStateChangeListener() { group, selectedChipIds ->
                val chip = group.findViewById<Chip>(group.checkedChipId)
                val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)

                mealTypeChip = selectedMealType
                mealTypeChipId = group.checkedChipId
                saveMealAndDietType()
            }

        mView.findViewById<ChipGroup>(R.id.dietType_chipGroup)
            .setOnCheckedStateChangeListener() { group, selectedChipIds ->
                val chip = group.findViewById<Chip>(group.checkedChipId)
                val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
                dietTypeChip = selectedDietType
                dietTypeChipId = group.checkedChipId
                saveMealAndDietType()
            }

        mView.findViewById<Button>(R.id.apply_btn).setOnClickListener {

            saveMealAndDietType()
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment(true)
            findNavController().navigate(action)
        }

        return mView
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesBottomSheet", e.message.toString())
            }
        }
    }
    private fun saveMealAndDietType() {
        recipesViewModel.saveMealAndDietType(
            mealTypeChip,
            mealTypeChipId,
            dietTypeChip,
            dietTypeChipId
        )
        recipesViewModel.applyOrderFood()
    }
}