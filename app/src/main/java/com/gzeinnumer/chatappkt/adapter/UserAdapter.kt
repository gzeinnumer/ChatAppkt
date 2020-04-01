package com.gzeinnumer.chatappkt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gzeinnumer.chatappkt.R
import com.gzeinnumer.chatappkt.databinding.UserItemBinding
import com.gzeinnumer.chatappkt.model.User

//todo 39
class UserAdapter(private val list: List<User>) :
    RecyclerView.Adapter<UserAdapter.MyHolder?>() {
    private var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding: UserItemBinding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context))
        context = parent.context
        return MyHolder(binding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyHolder(itemView: UserItemBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        fun bind(user: User) {
            binding.username.text = user.username
            if (user.imageURL == "default") {
                binding.profileImage.setImageResource(R.mipmap.ic_launcher)
            } else {
                Glide.with(context!!).load(user.imageURL).into(binding.profileImage)
            }
        }

        var binding: UserItemBinding = itemView
    }

}
