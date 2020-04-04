package com.gzeinnumer.chatappkt.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gzeinnumer.chatappkt.MessageActivity
import com.gzeinnumer.chatappkt.R
import com.gzeinnumer.chatappkt.databinding.UserItemBinding
import com.gzeinnumer.chatappkt.model.User

//todo 39
//class UserAdapter(private val list: List<User>) : RecyclerView.Adapter<UserAdapter.MyHolder?>() {

//todo 84 tambah variable untuk status dan perbaiki contructor
//todo 85 komentarkan costruktor diatas
class UserAdapter(private val list: List<User>, private val isChat: Boolean) : RecyclerView.Adapter<UserAdapter.MyHolder?>() {
//end todo 85
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: UserItemBinding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
//        holder.bind(list[position])
        //todo 86 komentarkan yang diatas
        holder.bind(list[position], isChat)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(itemView: UserItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
//        fun bind(user: User) {
        //todo 87 komentarkan yang diatas
        fun bind(user: User, isChat: Boolean) {
            binding.username.text = user.username
            if (user.imageURL == "default") {
                binding.profileImage.setImageResource(R.mipmap.ic_launcher)
            } else {
                Glide.with(context!!).load(user.imageURL).into(binding.profileImage)
            }

            //todo 47
            itemView.setOnClickListener {
                context!!.startActivity(Intent(context, MessageActivity::class.java).apply {
                    putExtra("id", user.id)
                })
            }

            //todo 88
            if (isChat) {
                if (user.status.equals("online")) {
                    binding.imgOn.visibility = View.VISIBLE
                    binding.imgOff.visibility = View.GONE
                } else {
                    binding.imgOn.visibility = View.GONE
                    binding.imgOff.visibility = View.VISIBLE
                }
            } else {
                binding.imgOn.visibility = View.GONE
                binding.imgOff.visibility = View.GONE
            }
            //end todo 88
        }

        var binding: UserItemBinding = itemView
    }

}
