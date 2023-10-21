package com.bernardooechsler.ecommerceapp.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bernardooechsler.ecommerceapp.R
import com.bernardooechsler.ecommerceapp.adapters.HomeViewpagerAdapter
import com.bernardooechsler.ecommerceapp.databinding.FragmentHomeBinding
import com.bernardooechsler.ecommerceapp.fragments.categories.AccessoryFragment
import com.bernardooechsler.ecommerceapp.fragments.categories.ChairFragment
import com.bernardooechsler.ecommerceapp.fragments.categories.CupboardFragment
import com.bernardooechsler.ecommerceapp.fragments.categories.FurnitureFragment
import com.bernardooechsler.ecommerceapp.fragments.categories.MainCategoryFragment
import com.bernardooechsler.ecommerceapp.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        val viewPager2Adapter =
            HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.viewpagerHome.adapter = viewPager2Adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()

        binding.viewpagerHome.isUserInputEnabled = false
    }
}