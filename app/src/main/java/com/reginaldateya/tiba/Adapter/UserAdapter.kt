package com.reginaldateya.tiba.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.reginaldateya.tiba.Model.User
import com.reginaldateya.tiba.ProfileActivity
import com.reginaldateya.tiba.R

class UserAdapter (private var mContext: Context,
                   private var mUser: List<User>,
                   private var isActivity: Boolean = false) : RecyclerView.Adapter<UserAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.patient_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        var user = mUser[position]
        holder.fullNameTextView.text = user.getFullName()
        holder.itemView.setOnClickListener(View.OnClickListener {
            val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.putString("profileId", user.getFullName())
            pref.apply()
            (mContext as Activity).finish()

            val profileIntent = Intent(mContext, ProfileActivity::class.java)
            profileIntent.putExtra("profileId", user.getFullName())
            mContext.startActivity(profileIntent)
        })

    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder(@NonNull itemView: View): RecyclerView.ViewHolder(itemView){
        var fullNameTextView: TextView = itemView.findViewById(R.id.full_name_search)
    }
}