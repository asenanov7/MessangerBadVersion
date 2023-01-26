package com.example.solomessenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

class RegistrationActivity : AppCompatActivity() {
    private lateinit var regEmail: EditText
    private lateinit var regPassword: EditText
    private lateinit var regName: EditText
    private lateinit var regSurname: EditText
    private lateinit var regAge: EditText
    private lateinit var regSign: Button
    private lateinit var progressBarRegistration: ProgressBar
    private lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        init()
        observeViewModel()
        regSign.setOnClickListener {
            val email = regEmail.text.toString().trim()
            val password = regPassword.text.toString().trim()
            val name = regName.text.toString().trim()
            val surname = regSurname.text.toString().trim()
            val age = regAge.text.toString().trim()
            if (email.isEmpty()||password.isEmpty()||name.isEmpty()||surname.isEmpty()||age.isEmpty()){
                return@setOnClickListener
            }
            registrationViewModel.createAndLogin(
                email,
                password,
                regName.text.trim().toString(),
                regSurname.text.trim().toString(),
                regAge.text.trim().toString()
                )
        }
    }

    private fun observeViewModel(){
        registrationViewModel.getLoadingLD().observe(this){
            if (it){
                progressBarRegistration.visibility = View.VISIBLE
            }else{
                progressBarRegistration.visibility = View.INVISIBLE
            }
        }
        registrationViewModel.getErrorLD().observe(this){
            if (it!=null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        registrationViewModel.getSuccessLD().observe(this){
            if (it!=null) {
                finish()
                startActivity(ChatsActivity.newIntent(this))
            }
        }
    }
    private fun init() {
        regEmail = findViewById(R.id.regEmail)
        regPassword = findViewById(R.id.regPassword)
        regName = findViewById(R.id.regName)
        regSurname = findViewById(R.id.regSurname)
        regAge = findViewById(R.id.regAge)
        regSign = findViewById(R.id.regSign)
        progressBarRegistration = findViewById(R.id.progressBarRegistration)
        registrationViewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
    }
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RegistrationActivity::class.java)
        }
    }
}