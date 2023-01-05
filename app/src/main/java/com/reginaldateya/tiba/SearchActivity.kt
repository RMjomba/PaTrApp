package com.reginaldateya.tiba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reginaldateya.tiba.Adapter.UserAdapter
import com.reginaldateya.tiba.Model.User
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var userAdapter: UserAdapter? = null
    private var mUser: MutableList<User>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recyclerView = findViewById(R.id.recycler_view_search)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(baseContext)

        mUser = ArrayList()
        userAdapter = baseContext?.let { UserAdapter(it, mUser as ArrayList<User>, true) }
        recyclerView?.adapter = userAdapter

        etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (etSearch.text.toString() == "")
                {

                }
                else{
                    recyclerView?.visibility = View.VISIBLE

                    retrieveUsers()
                    searchUser(p0.toString().toLowerCase())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

            private fun searchUser(input: String)
            {
                val query = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .orderByChild("fullname")
                    .startAt(input)
                    .endAt(input + "\uf8ff")
                query.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        mUser?.clear()

                        for (snapshot in dataSnapshot.children)
                        {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null)
                            {
                                mUser?.add(user)
                            }
                        }

                        userAdapter?.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        })
    }

    private fun retrieveUsers() {
        val userRef = FirebaseDatabase.getInstance().getReference().child("Users")
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (etSearch.text.toString() == "")
                {
                    mUser?.clear()

                    for (snapshot in dataSnapshot.children)
                    {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null)
                        {
                            mUser?.add(user)
                        }
                    }

                    userAdapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}