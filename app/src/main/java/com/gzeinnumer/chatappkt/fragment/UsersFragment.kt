package com.gzeinnumer.chatappkt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.gzeinnumer.chatappkt.adapter.UserAdapter
import com.gzeinnumer.chatappkt.databinding.FragmentUsersBinding
import com.gzeinnumer.chatappkt.model.User

/**
 * A simple [Fragment] subclass.
 */
class UsersFragment : Fragment() {

    //todo 40
    private lateinit var binding: FragmentUsersBinding
    private lateinit var myUserAdapter: UserAdapter
    private val list: ArrayList<User> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //todo 41
        binding = FragmentUsersBinding.inflate(layoutInflater, container, false);
        return binding.getRoot();
    }

    //todo 42
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readData()
    }

    //todo 43
    private fun readData() {
        Log.d("MyZein", "Aman")
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference =
            FirebaseDatabase.getInstance().getReference("Users_chat_app")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    if (user.id != firebaseUser!!.uid) {
                        list.add(user)
                    }
                }
                Log.d("MyZein", list.size.toString())

//                myUserAdapter = UserAdapter(list)
                //todo 94 komentari yang diatas
                myUserAdapter = UserAdapter(list, true)
                //end todo 94

                binding.rvData.apply {
                    adapter = myUserAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("MyZein", "Ada error")
            }
        })
    }

}
