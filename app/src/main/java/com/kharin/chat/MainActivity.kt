package com.kharin.chat

import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kharin.chat.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        setUpActionBar()
        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.apply {
                bSend.setOnClickListener {
                myRef.child(myRef.push().key?:"error") // child - new /path in the database
                    .setValue(UserMessage(auth.currentUser?.displayName, binding.etMessage.text.toString()))
                etMessage.text.clear()
            }
        }
        initRCView()
        updateChat(myRef)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mi_sign_out){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
   private fun initRCView() = with(binding){
        adapter = MessageAdapter()
        rvChat.layoutManager = LinearLayoutManager(this@MainActivity)
        rvChat.adapter = adapter
   }
    private fun updateChat(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    val list = ArrayList<UserMessage>()
                    for(s in snapshot.children){
                        val userMessage = s.getValue(UserMessage::class.java)
                        if(userMessage!=null) list.add(userMessage)
                    }
                    adapter.submitList(list)
                    rvChat.smoothScrollToPosition(adapter.itemCount)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setUpActionBar(){
        val actionBar = supportActionBar
        Thread {
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get() //download from google account
            val dIcon = BitmapDrawable(resources,bMap) //create drawable from picture
            runOnUiThread{
                actionBar?.setDisplayHomeAsUpEnabled(true) // show @home@ button in the ActionBar
                actionBar?.setHomeAsUpIndicator(dIcon)
                actionBar?.title = auth.currentUser?.displayName
            }
        }.start()
    }
}