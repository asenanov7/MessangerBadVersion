package com.example.solomessenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity() {
    private lateinit var editTextEmail:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var buttonSign: Button
    private lateinit var textViewRegistration:TextView
    private lateinit var textViewReset:TextView
    private lateinit var progressBarMain: ProgressBar
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        observeViewModel()
        setOnClickListeners()
    }
    private fun init(){
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSign = findViewById(R.id.buttonSign)
        textViewRegistration = findViewById(R.id.textViewRegistration)
        textViewReset = findViewById(R.id.textViewReset)
        progressBarMain = findViewById(R.id.progressBarMain)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    private fun observeViewModel(){
        mainViewModel.getErrorLD().observe(this){
            if (it!=null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        mainViewModel.getLoadingLD().observe(this){
            if (it){
                progressBarMain.visibility = View.VISIBLE
            }else{
                progressBarMain.visibility = View.INVISIBLE
            }
        }
        mainViewModel.getSuccessLD().observe(this){
            if (it!=null) {
                finish()
                startActivity(ChatsActivity.newIntent(this))
            }
        }
    }
    private fun setOnClickListeners(){
        buttonSign.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            if (email.isEmpty()||password.isEmpty()){ return@setOnClickListener }
            mainViewModel.sign(email, password)
        }
        textViewRegistration.setOnClickListener {
            startActivity( RegistrationActivity.newIntent(this) )
        }
        textViewReset.setOnClickListener {
            startActivity(ResetActivity.newIntent(this, editTextEmail.text.toString().trim()))
        }
    }
    companion object{
        fun newIntent(context: Context):Intent{
            return Intent(context, MainActivity::class.java)
        }
    }
}