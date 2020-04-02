package com.gzeinnumer.chatappkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.gzeinnumer.chatappkt.R
import com.gzeinnumer.chatappkt.model.Chat

//todo 56
class MessageAdapter(private val list: List<Chat>, private val image: String) :
    RecyclerView.Adapter<MessageAdapter.MyHolder>() {
    var context: Context? = null
    var firebaseUser: FirebaseUser? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        context = parent.context
        return if (viewType == MSG_TYPE_RIGHT) {
            val bindingR: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_right, parent, false)
            MyHolder(bindingR)
        } else {
            val bindingL: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_left, parent, false)
            MyHolder(bindingL)
        }
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position], image, context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        var showMessage: TextView = itemView.findViewById(R.id.show_message)

        fun bind(chat: Chat, image: String, context: Context?) {
            showMessage.text = chat.message
            if (image == "default") {
                profileImage.setImageResource(R.mipmap.ic_launcher)
            } else {
                Glide.with(context!!).load(image).into(profileImage)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (list[position].sender.equals(firebaseUser!!.uid)) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    companion object {
        private const val MSG_TYPE_LEFT = 0
        private const val MSG_TYPE_RIGHT = 1
    }

}