package com.example.foodrecipesapplication.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.foodrecipesapplication.R

class RecipeBindingAdapter {
    companion object {
        @BindingAdapter("setNumberOfLikes")
        @JvmStatic
        fun numberOfLikes(textView: TextView, numberOfLikes: Int) {
            textView.text = numberOfLikes.toString()
        }

        @BindingAdapter("timeTaken")
        @JvmStatic
        fun timeToForCooking(textView: TextView, time: Int) {
            textView.text = time.toString()
        }

        @BindingAdapter("isVegan")
        @JvmStatic
        fun isVegan(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }

                    is ImageView -> {
                        view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                    }
                }
            }
        }
    }
}