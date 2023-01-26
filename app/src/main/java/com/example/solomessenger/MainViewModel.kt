package com.example.solomessenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainViewModel:ViewModel() {
    private val auth = FirebaseAuth.getInstance()


    private val loadingLD = MutableLiveData<Boolean>()
    fun getLoadingLD():LiveData<Boolean> = loadingLD

    private val successLD = MutableLiveData<FirebaseUser>()
    fun getSuccessLD():LiveData<FirebaseUser> = successLD

    private val errorLD = MutableLiveData<String>()
    fun getErrorLD():LiveData<String> = errorLD

    fun sign(email: String, password: String){
        loadingLD.value = true

        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { val user = auth.currentUser; successLD.value = user }
            .addOnFailureListener { errorLD.value = it.message  }
            .addOnCompleteListener { loadingLD.value = false  }
    }
    init {
        auth.addAuthStateListener {
        val user = it.currentUser
            if (user!=null){
                successLD.value =user
            }
        }
    }
}