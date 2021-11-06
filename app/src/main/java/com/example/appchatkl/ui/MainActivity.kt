package com.example.appchatkl.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.appchatkl.R
import com.example.appchatkl.databinding.ActivityMainBinding
import com.example.appchatkl.ui.content.BottomFragment
import com.example.appchatkl.ui.content.allFriend.AllFriendFragment
import com.example.appchatkl.ui.content.listMessage.ListMessageFragment
import com.example.appchatkl.ui.content.requestFriend.requestFriendFragment
import com.example.appchatkl.ui.login.LoginFragment

open class MainActivity : AppCompatActivity() {

    val TAG="MainActivity"

    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.framentActivity, BottomFragment.newInstance())
//        transaction.disallowAddToBackStack()
//        transaction.commit()

    }

    override open fun onBackPressed() {
        super.onBackPressed()
    }


}