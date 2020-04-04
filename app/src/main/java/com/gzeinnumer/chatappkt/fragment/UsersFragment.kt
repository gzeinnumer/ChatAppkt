package com.gzeinnumer.chatappkt.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        //todo 96
        binding.searchUser.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                //searchUser(s.toString());
                //todo 104 komentarkan yang diatas
                searchUser(s.toString().toLowerCase())
                //end todo 104
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.imgClear.setOnClickListener {
            binding.searchUser.text.clear()
            binding.searchUser.clearFocus()
        }
    }

    //todo 43
    private fun readData() {
        Log.d("MyZein", "Aman")
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference =
            FirebaseDatabase.getInstance().getReference("Users_chat_app")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //todo 98-1 tambahkan if
                if(binding.searchUser.getText().toString().equals("")) {
                //end todo 98-1
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

                //todo 98-2
                }
                //end todo 98-2
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("MyZein", "Ada error")
            }
        })
    }


    //todo 97
    private fun searchUser(s: String) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        //Query query = FirebaseDatabase.getInstance().getReference("Users_chat_app").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        //todo 103 komentarkan yg di atas
        val query = FirebaseDatabase.getInstance().getReference("Users_chat_app").orderByChild("search").startAt(s).endAt(s + "\uf8ff")
        //end todo 103
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    assert(firebaseUser != null)
                    if (!user.id.equals(firebaseUser!!.uid)) {
                        list.add(user)
                    }
                }
                myUserAdapter = UserAdapter(list, true)
                binding.rvData.adapter = myUserAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
