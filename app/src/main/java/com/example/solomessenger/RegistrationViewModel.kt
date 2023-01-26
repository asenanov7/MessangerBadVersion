package com.example.solomessenger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.database
    private val refUsers = database.getReference("Users")

    private val loadingLD = MutableLiveData<Boolean>()
    fun getLoadingLD(): LiveData<Boolean> = loadingLD

    private val successLD = MutableLiveData<FirebaseUser>()
    fun getSuccessLD(): LiveData<FirebaseUser> = successLD

    private val errorLD = MutableLiveData<String>()
    fun getErrorLD(): LiveData<String> = errorLD

    fun createAndLogin(
        email: String,
        password: String,
        name: String,
        surname: String,
        age: String
    ) {
        loadingLD.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //здесь мы говорим в firebase по ключу юсерс найди ребенка с ключом (id пользователя) и добавь туда пользователя с этим ключем
                //по сути он такой, блять такого ключа нету, но я срочно его создам
                refUsers.child(it.user!!.uid).setValue(User(it.user!!.uid, name, surname, age))
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { val user = auth.currentUser; successLD.value = user }
                    .addOnFailureListener { errorLD.value = it.message }
                    .addOnCompleteListener { loadingLD.value = false }
            }
            .addOnFailureListener { errorLD.value = it.message; loadingLD.value = false }
    }
}