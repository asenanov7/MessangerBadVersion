package com.example.solomessenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ResetViewModel:ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val loadingLD = MutableLiveData<Boolean>()
    fun getLoadingLD(): LiveData<Boolean> = loadingLD

    private val successLD = MutableLiveData<Boolean>()
    fun getSuccessLD(): LiveData<Boolean> = successLD

    private val errorLD = MutableLiveData<String>()
    fun getErrorLD(): LiveData<String> = errorLD

    fun reset(email:String){
        loadingLD.value = true
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { successLD.value = true }
            .addOnFailureListener { errorLD.value = it.message }
            .addOnCompleteListener { loadingLD.value = false }
    }
}