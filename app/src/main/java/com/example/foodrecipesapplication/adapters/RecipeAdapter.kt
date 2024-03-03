package com.example.foodrecipesapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipesapplication.databinding.RowRecipeBinding
import com.example.foodrecipesapplication.models.Recipe

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    class RecipeViewHolder(private val binding: RowRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipe = recipe
            binding.executePendingBindings()
        }
    }

    private val recipesDifferList = object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem === newItem

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }

    private var recipes = AsyncListDiffer(this, recipesDifferList)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = RowRecipeBinding.inflate(from, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = this.recipes.currentList.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = this.recipes.currentList[position]
        holder.bind(currentRecipe)
    }
}