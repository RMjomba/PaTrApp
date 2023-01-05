package com.reginaldateya.tiba

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reginaldateya.tiba.Model.PostInfo
import com.reginaldateya.tiba.Model.Ticket
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.activity_contact.view.*
import kotlinx.android.synthetic.main.add_ticket.view.*
import kotlinx.android.synthetic.main.contacts_ticket.view.*

class ContactActivity : AppCompatActivity() {

    var listContacts = ArrayList<Ticket>()
    var adapter :MyContactAdapter? = null
    var UserUID : String? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        supportActionBar?.hide()




        listContacts.add(Ticket("0","name","number","number", "name", "number", "text", "number","name", "number","number"))
        listContacts.add(Ticket("0","name","number","number", "name", "number", "text", "number","name", "number","number"))
        listContacts.add(Ticket("0","name","number","number", "name", "number", "text", "number","name", "number","number"))
        listContacts.add(Ticket("0","name","number","number", "name", "number", "text", "number","name", "number","number"))
        listContacts.add(Ticket("0","name","number","number", "name", "number", "text", "number","name", "number","number"))







        adapter = MyContactAdapter(this, listContacts)
        lvContacts.adapter = adapter
        loadPost()

    }


    inner class MyContactAdapter:BaseAdapter{
        var listContactsAdapter = ArrayList<Ticket>()
        var context: Context? = null
        constructor(context: Context, listContactsAdapter:ArrayList<Ticket>):super(){
            this.listContactsAdapter = listContactsAdapter
            this.context = context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val myContact = listContactsAdapter[p0]


            if (myContact.UserUID.equals("0")) {

                //Load add ticket
                var myView = layoutInflater.inflate(R.layout.add_ticket, null)

                myView.publish.setOnClickListener(View.OnClickListener {
                    //Upload server
                    myRef.child("posts").push().setValue(PostInfo(
                        UserUID.toString(),
                        myView.etFullName.text.toString(),
                        myView.etIdNumber.text.toString(),
                        myView.etRegistryNumber.text.toString(),
                        myView.etClinicName.text.toString(),
                        myView.etInterviewDate.text.toString(),
                        myView.etGender.text.toString(),
                        myView.etPhoneNumber.text.toString(),
                        myView.district.text.toString(),
                        myView.etOriginationDate.text.toString(),
                        myView.etLastRevisionDate.text.toString()))


                    listContacts.removeAt(0)
                    adapter?.notifyDataSetChanged()


                })
                return myView

            } else {
                //Load  contacts ticket
                var myView = layoutInflater.inflate(R.layout.contacts_ticket, null)
                myView.tvFullName.setText(myContact.fullName)
                myView.tvIdNumber.setText(myContact.idNumber)
                myView.tvRegistryNumber.setText(myContact.registryNumber)
                myView.tvClinicName.setText(myContact.clinicName)
                myView.tvInterviewDate.setText(myContact.interviewDate)
                myView.tvGender.setText(myContact.gender)
                myView.tvMobileNumber.setText(myContact.phoneNumber)
                myView.tvDistrict.setText(myContact.district)
                myView.tvOriginationDate.setText(myContact.originationDate)
                myView.tvLastRevisionDate.setText(myContact.lastRevisionDate)

                return myView

            }




        }

        override fun getCount(): Int {
            return listContactsAdapter.size
        }

        override fun getItem(p0: Int): Any {
            return listContactsAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }


    }

    private fun loadPost(){
        myRef.child("post")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {

                        listContacts.clear()
                        listContacts.add(Ticket("0","name","number","number", "name", "number", "text", "number","name", "number","number"))

                        var td = dataSnapshot!!.value as HashMap<String, Any>
                        for ( key in td.keys){
                            var post = td[key] as HashMap<String, Any>
                            listContacts.add(Ticket(key,
                                post["fullName"] as String,
                                post["idNumber"] as String,
                                post["registryNumber"] as String,
                                post["clinicName"] as String,
                                post["interviewDate"] as String,
                                post["gender"] as String,
                                post["phoneNumber"] as String,
                                post["district"] as String,
                                post["originationDate"] as String,
                                post["lastRevisionDate"] as String))

                        }

                        adapter!!.notifyDataSetChanged()

                    } catch (ex:Exception){}
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }



}