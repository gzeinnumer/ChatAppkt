package com.gzeinnumer.chatappkt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.gzeinnumer.chatappkt.adapter.UserAdapter
import com.gzeinnumer.chatappkt.databinding.FragmentChatsBinding
import com.gzeinnumer.chatappkt.model.Chat
import com.gzeinnumer.chatappkt.model.User
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : Fragment() {


    //todo 61
    private lateinit var binding: FragmentChatsBinding
    private lateinit var myUserAdapter: UserAdapter
    private var userListFromUsers: ArrayList<User> = ArrayList()
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var reference: DatabaseReference
    private val userListFromChat: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //todo 62
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.root;
    }

    //todo 63
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        reference = FirebaseDatabase.getInstance().getReference("Chats_app")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userListFromChat.clear()
                for (snapshot in dataSnapshot.children) {
                    val chat: Chat = snapshot.getValue(Chat::class.java)!!
                    if (chat.sender.equals(firebaseUser.uid)) {
                        chat.receiver?.let { userListFromChat.add(it) }
                    }
                    if (chat.receiver.equals(firebaseUser.uid)) {
                        chat.sender?.let { userListFromChat.add(it) }
                    }
                }
                readChats()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    //todo 64
    private fun readChats() {
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userListFromUsers.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    for (id in userListFromChat) {
                        if (user.id.equals(id)) {
                            if (userListFromUsers.size != 0) {
                                for (userYangTersedia in userListFromUsers) {
                                    if (!user.id.equals(userYangTersedia.id)) {
                                        userListFromUsers.add(user)
                                    }
                                }
                            } else {
                                userListFromUsers.add(user)
                            }
                        }
                    }
                }
                binding.rvData.setHasFixedSize(true)
                binding.rvData.layoutManager = LinearLayoutManager(context)
                myUserAdapter = UserAdapter(userListFromUsers)
                binding.rvData.adapter = myUserAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
