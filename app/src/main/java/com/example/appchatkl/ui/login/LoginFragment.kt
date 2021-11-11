package com.example.appchatkl.ui.login

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.db.AppDatabase
import com.example.appchatkl.data.db.ChatDao
import com.example.appchatkl.data.db.Data.Save
import com.example.appchatkl.databinding.LoginFragmentBinding
import com.example.appchatkl.ui.MainActivity
import com.example.appchatkl.ui.content.friend.friend.FriendFragment
import com.example.appchatkl.ui.register.RegisterViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    val TAG = "LoginFragment"
    private lateinit var controller: NavController
    lateinit var chatDB: AppDatabase

    companion object {
        fun newInstance() = FriendFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding: LoginFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.login_fragment, container, false
        )
        var view: View = binding.root
        binding.lifecycleOwner = this
        chatDB = AppDatabase.getDatabase(view.context)
        val loginViewModel: LoginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.login = loginViewModel
        controller = findNavController()
        binding.txtRegister.setOnClickListener {
            controller.navigate(R.id.registerFragment2)
        }
        binding.btnDn.setOnClickListener {
            loginViewModel.onLogin()

        }
        loginViewModel.isCheck.observe(viewLifecycleOwner, {
            Log.d(TAG, "onCreateView: " + it)
            if (it) {
                controller.navigate(R.id.bottomFragment)
            }
        })


        return view
    }


    override fun onStart() {
        super.onStart()
        var auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val emailVerified: Boolean = currentUser.isEmailVerified();
            val uid = currentUser.uid
            chatDB.chatDao().insertSave(Save(uid))
            controller.navigate(R.id.bottomFragment)
        } else {
            chatDB.chatDao().loadSave().forEach {
                if (!it.id.equals("null")) {
                    controller.navigate(R.id.bottomFragment)
                }
            }
        }

    }

}