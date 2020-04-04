package com.gzeinnumer.chatappkt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.gzeinnumer.chatappkt.R
import com.gzeinnumer.chatappkt.databinding.FragmentProfileBinding
import com.gzeinnumer.chatappkt.model.User

/**
 * A simple [Fragment] subclass.
 */
//todo 65 part 10
class ProfileFragment : Fragment() {

    //todo 67
    lateinit var binding: FragmentProfileBinding
    lateinit var reference: DatabaseReference
    var firebaseUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //todo 68
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    //todo 69
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users_chat_app").child(firebaseUser!!.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User = dataSnapshot.getValue(User::class.java)!!
                binding.username.text = user.username
                if (user.imageURL.equals("default")) {
                    binding.profileImage.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(view.context).load(user.imageURL)
                        .into(binding.profileImage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}
