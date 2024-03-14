package com.example.foodrecipesapplication.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.foodrecipesapplication.databinding.FragmentRecipesFilterBinding

class RecipesFilterFragment : Fragment() {
    private lateinit var binding: FragmentRecipesFilterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        this.binding = FragmentRecipesFilterBinding.inflate(inflater, container, false)
        return this.binding.root
    }
}