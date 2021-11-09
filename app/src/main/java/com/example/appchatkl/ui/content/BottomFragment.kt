package com.example.appchatkl.ui.content

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.BottomFragmentBinding
import com.example.appchatkl.ui.content.Find.Adapter.FindMessageAdapter
import com.example.appchatkl.ui.content.listMessage.adapter.onClick
import com.example.appchatkl.ui.content.user.BottomViewModel
import com.example.appchatkl.ui.content.user.Find
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val FIND_MESSAGE=1
private const val FIND_FRIEND=2

class BottomFragment : Fragment(),onClick {
    private var  FIND:Int = 0
    val TAG="BottomFragment"
    lateinit var find: Find
    //lateinit var controller:NavController
    val viewModel:BottomViewModel by activityViewModels()
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
       // viewModel = ViewModelProvider(this).get(BottomViewModel::class.java)
        binding.lifecycleOwner = this
        val loginDialogContainer = childFragmentManager.findFragmentById(R.id.frag) as NavHostFragment
        val loginNavController: NavController = loginDialogContainer.navController
        binding.imgCreateMess.setOnClickListener{
           val controller=findNavController()
            controller.navigate(R.id.createConversationFragment)
        }

        binding.bottomNavigation.setupWithNavController(loginNavController)
        binding.bottomNavigation.setOnClickListener{
            binding.linear1.visibility=View.GONE
       binding.viewTop.visibility=View.GONE
        }
        loginNavController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.userFragment) {
                binding.linear1.visibility=View.GONE
                binding.viewTop.visibility=View.GONE
            }else{
                binding.linear1.visibility=View.VISIBLE
                binding.viewTop.visibility=View.VISIBLE
                if(destination.id == R.id.listMessageFragment) FIND= FIND_MESSAGE
                else FIND= FIND_FRIEND
            }
        }
        var auth: FirebaseAuth = Firebase.auth
        val currentUser: FirebaseUser? = auth.currentUser
        val id= commomFunction.getId(currentUser!!).toString()
        val database: DatabaseReference
        database = Firebase.database.reference

        //notifical
        val list=ArrayList<Conversation>()
        viewModel.getChat(database,list,id)
        viewModel.count.observe(viewLifecycleOwner,{
            val count=it.toInt()
            if(count>0) {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.listMessageFragment).apply {
                        isVisible = true
                        number = count
                    }
            }else {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.listMessageFragment).apply {
                        isVisible = false
                        number = count
                    }
            }
        })
        viewModel.getInvitationAndRequest(database,id)
        viewModel.countRecive.observe(viewLifecycleOwner,{
            val count=it.toInt()

            if(count>0) {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.tablayouFiendFragment).apply {
                        isVisible = true
                        number = count
                    }
            }else {
                var badge =
                    binding.bottomNavigation.getOrCreateBadge(R.id.tablayouFiendFragment).apply {
                        isVisible = false
                        number = count
                    }
            }
        })
        ///find

        binding.edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.edt.value=newText
                }
                return true
            }

        })



        // Log.d(TAG, "onCreateView: "+binding.bottomNavigation.selectedItemId)
        return view
    }

    override fun openChat(s: String) {
        TODO("Not yet implemented")
    }



}