package com.reginaldateya.tiba

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.reginaldateya.tiba.Model.Test
import kotlinx.android.synthetic.main.activity_lab.*
import kotlinx.android.synthetic.main.add_test.view.*
import kotlinx.android.synthetic.main.tests.view.*

class LabActivity : AppCompatActivity() {

    var listTests = ArrayList<Test>()
    var adapter : LabActivity.MyTestAdapter? = null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Lab Test"

        listTests.add(Test("0", "Entry"))



        adapter = MyTestAdapter(this, listTests)
        lvTests.adapter = adapter
        loadPost()
    }


    inner class MyTestAdapter: BaseAdapter {
        var listTestsAdapter = ArrayList<Test>()
        var context: Context? = null
        constructor(context: Context, listTestsAdapter:ArrayList<Test>):super(){
            this.listTestsAdapter = listTestsAdapter
            this.context = context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val myTest = listTestsAdapter[p0]


            if (myTest.testID.equals("0")) {
                var myView = layoutInflater.inflate(R.layout.add_test, null)

                //Load add ticket

                myView.post.setOnClickListener(View.OnClickListener {

                    myRef.child("testPost").push().child("text").setValue(myView.etLabTest.text.toString())
                })

                return myView

            } else {
                var myView = layoutInflater.inflate(R.layout.tests, null)
                //Load  contacts ticket
                myView.tvLabTest.setText(myTest.testText)


                return myView

            }




        }

        override fun getCount(): Int {
            return listTestsAdapter.size
        }

        override fun getItem(p0: Int): Any {
            return listTestsAdapter[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }


    }

    private fun loadPost(){
        myRef.child("testPost")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    try {

                        listTests.clear()
                        listTests.add(Test("0", "Entry"))

                        var td = dataSnapshot!!.value as HashMap<*, *>
                        for (key in td.keys){
                            var post = td[key] as HashMap<*, *>
                            listTests.add(Test(
                                key as String,
                                post["text"] as String))
                        }

                        adapter!!.notifyDataSetChanged()
                    }catch (ex:Exception){}
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}