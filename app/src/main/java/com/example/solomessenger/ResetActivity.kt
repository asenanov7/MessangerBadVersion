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

class ResetActivity : AppCompatActivity() {
    private lateinit var resetEmail:EditText
    private lateinit var resetButton:Button
    private lateinit var progressBarReset:ProgressBar
    private lateinit var resetViewModel: ResetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)

        init()
        resetEmail.setText(intent.getStringExtra("email"))
        observeViewModel()
        resetButton.setOnClickListener {
            val email = resetEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                resetViewModel.reset(email)
            }
        }

    }
    private fun observeViewModel(){
        resetViewModel.getErrorLD().observe(this){
            if (it!=null){
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        resetViewModel.getLoadingLD().observe(this){
            if (it){
                progressBarReset.visibility = View.VISIBLE
            }else{
                progressBarReset.visibility = View.INVISIBLE
            }
        }
        resetViewModel.getSuccessLD().observe(this){
            if (it){
                finish()
                startActivity(MainActivity.newIntent(this))
                Toast.makeText(application, "Отправлено", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun init(){
        resetEmail = findViewById(R.id.resetEmail)
        resetButton = findViewById(R.id.resetButton)
        progressBarReset = findViewById(R.id.progressBarReset)
        resetViewModel = ViewModelProvider(this)[ResetViewModel::class.java]
    }
    companion object{
        fun newIntent(context: Context, email:String):Intent{
            val intent = Intent(context, ResetActivity::class.java)
            intent.putExtra("email", email)
            return intent
        }
    }
}