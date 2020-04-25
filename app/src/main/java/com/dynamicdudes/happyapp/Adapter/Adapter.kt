package com.dynamicdudes.happyapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dynamicdudes.happyapp.R
import com.dynamicdudes.happyapp.User
import kotlinx.android.synthetic.main.user_contact_row.view.*

class Adapter(val users: List<User>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val userName = itemView.name_text_view
        val userImageView = itemView.user_profile_image
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_contact_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.userName.text = "${user.username}"
        Glide.with(holder.itemView.context)
            .load(user.profileImageUrl)
            .into(holder.userImageView)
    }
}