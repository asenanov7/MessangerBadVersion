package com.example.solomessenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.platforminfo.LibraryVersionComponent

class MessagesViewModel(val reciverID: String = "") : ViewModel() {
    private val auth = Firebase.auth
    private val firebaseDatabase = Firebase.database
    private val refMessages = firebaseDatabase.getReference("Messages")

    fun getCurrentUserid() = auth.currentUser!!.uid

    private val errorLD = MutableLiveData<String>()
    fun getErrorLD(): LiveData<String> = errorLD

    private val messagesLD = MutableLiveData<List<Message>>()
    fun getMessagesLD(): LiveData<List<Message>> = messagesLD

    private val successLD = MutableLiveData<Boolean>()
    fun getSuccessLD(): LiveData<Boolean> = successLD

    private val statusLD = MutableLiveData<Boolean>()
    fun getStatusLD(): LiveData<Boolean> = statusLD


    fun sendMessage(message: Message) {
        refMessages
            .child(message.senderUserID)
            .child(message.reciverUserID)
            .push()
            .setValue(message)
            .addOnSuccessListener {
                successLD.value = true
                refMessages
                    .child(message.reciverUserID)
                    .child(message.senderUserID)
                    .push()
                    .setValue(message)
                    .addOnFailureListener {
                        errorLD.value = it.message
                    }
            }
            .addOnFailureListener {
                errorLD.value = it.message
            }
    }

    init {
        refMessages.child(auth.currentUser!!.uid).child(reciverID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val temp = mutableListOf<Message>()
                    for (item in snapshot.children) {
                        temp.add(item.getValue(Message::class.java)!!)
                    }
                    messagesLD.value = temp
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        firebaseDatabase.getReference("Users").child(reciverID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                     val user = snapshot.getValue(User::class.java)
                    statusLD.value = user!!.status
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun setStatus(status: Boolean) {
        if (auth.currentUser == null) {
            return
        }
        val temp = refMessages.child(auth.currentUser!!.uid).child("status")
        temp.setValue(status)
    }


}