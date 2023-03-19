package com.kharin.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kharin.chat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.apply {
                bSend.setOnClickListener {
                myRef.setValue(etMessage.text.toString())
                etMessage.text.clear()
            }
        }
        updateChat(myRef)
    }
    private fun updateChat(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    textView.append("\n")
                    textView.append("Alex: ${snapshot.value.toString()}")
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}