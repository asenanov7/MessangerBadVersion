package com.example.solomessenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class ChatViewModel:ViewModel() {
    private val auth = Firebase.auth
    private val firebaseDatabase = Firebase.database
    private val refUsers = firebaseDatabase.getReference("Users")

    private val usersLD = MutableLiveData<List<User>>()
    fun getUsersLD(): LiveData<List<User>> = usersLD

    private val userOuted = MutableLiveData<Boolean>()
    fun getUseOuted():LiveData<Boolean> =userOuted

    fun setStatus(status:Boolean){
        if (auth.currentUser==null){
            return
        }
       val temp =  refUsers.child(auth.currentUser!!.uid).child("status")
            temp.setValue(status)
    }

    init {
        auth.addAuthStateListener {
            val user  = it.currentUser
            if (user==null){
                userOuted.value = true
            }
        }

        refUsers.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = mutableListOf<User>()
                for (item in snapshot.children) {
                    val user = item.getValue(User::class.java)!!
                    if (user.id!=auth.currentUser!!.uid) {
                        temp.add(user)
                    }
                }
                usersLD.value = temp
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
    fun signOut(){
        setStatus(false)
        auth.signOut()
    }
}