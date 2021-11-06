package com.example.appchatkl.ui.content

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.appchatkl.R
import com.example.appchatkl.databinding.BottomFragmentBinding
import com.example.appchatkl.ui.MainActivity
import com.example.appchatkl.ui.login.LoginViewModel


class BottomFragment : Fragment() {
    //lateinit var controller:NavController
    companion object {
        fun newInstance() = BottomFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var binding: BottomFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_fragment, container, false
        )
        var view: View = binding.root
        binding.lifecycleOwner = this
        val loginDialogContainer = childFragmentManager.findFragmentById(R.id.frag) as NavHostFragment
        val loginNavController: NavController = loginDialogContainer.navController

        binding.bottomNavigation.setupWithNavController(loginNavController)
        return view
    }

}