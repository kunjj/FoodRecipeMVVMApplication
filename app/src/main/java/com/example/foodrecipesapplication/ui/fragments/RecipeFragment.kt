package com.example.foodrecipesapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipesapplication.FoodRecipeApplication
import com.example.foodrecipesapplication.adapters.RecipeAdapter
import com.example.foodrecipesapplication.databinding.FragmentRecipeBinding
import com.example.foodrecipesapplication.network.NetworkResult
import com.example.foodrecipesapplication.utils.Constant
import com.example.foodrecipesapplication.viewmodels.FoodRecipesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private val recipeAdapter by lazy { RecipeAdapter() }
    private lateinit var viewModel: FoodRecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecipeBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.viewModel = ViewModelProvider(this)[FoodRecipesViewModel::class.java]
        setUpRecyclerView()
        viewModel.getRandomFoodsRecipes(queries())

        viewModel.foodRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    stopShimmerEffect()
                    response.data?.let { recipeAdapter.recipes.submitList(it.recipes.toList()) }
                }

                is NetworkResult.Error -> {
                    stopShimmerEffect()
                    Snackbar.make(view, response.message.toString(), Snackbar.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }

        }
    }

    private fun queries(): HashMap<String, String> {
        val queries = HashMap<String, String>()
        queries["apiKey"] = Constant.API_KEY
        queries["number"] = "1"
        return queries
    }

    private fun setUpRecyclerView() = binding.recyclerView.apply {
        adapter = recipeAdapter
        layoutManager = LinearLayoutManager(FoodRecipeApplication.getApplicationContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() = binding.recyclerView.showShimmer()

    private fun stopShimmerEffect() = binding.recyclerView.hideShimmer()
}