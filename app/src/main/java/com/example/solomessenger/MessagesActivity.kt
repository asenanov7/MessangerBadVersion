package com.example.solomessenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class MessagesActivity : AppCompatActivity() {
    private lateinit var textViewHeader:TextView
    private lateinit var statusMes:ImageView
    private lateinit var recyclerMessages:RecyclerView
    private lateinit var editTextMessage:EditText
    private lateinit var imageViewSend:ImageView
    private lateinit var adapterMessages:AdapterMessages
    private lateinit var viewModel:MessagesViewModel
    private lateinit var reciverID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        val user = intent.getSerializableExtra("user") as User
        reciverID = user.id
        init()

        val username = "${user.name} ${user.surname}"
        textViewHeader.text = username

        observeViewModel()
        imageViewSend.setOnClickListener {
            val text = editTextMessage.text.toString().trim()
            val message = Message(viewModel.getCurrentUserid(),reciverID.toString(),text)
            if (text.isNotEmpty()) {
                viewModel.sendMessage(message)
            }
        }

    }

    private fun observeViewModel(){
        viewModel.getStatusLD().observe(this){
            val onlineDrawable = ContextCompat.getDrawable(this, android.R.drawable.presence_online)
            val offlineDrawable = ContextCompat.getDrawable(this, android.R.drawable.presence_offline )
            if (it) {
              statusMes.setImageDrawable(onlineDrawable)
            }else{
                statusMes.setImageDrawable(offlineDrawable)
            }
        }

        viewModel.getMessagesLD().observe(this){
            adapterMessages.setDataOnAdapterMessageList(it)
        }
        viewModel.getErrorLD().observe(this){
            if (it!=null){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getSuccessLD().observe(this){
            if (it) {
                editTextMessage.setText("")
            }
        }
    }

    private fun init(){
        val factory = ViewModelFactory(reciverID)
        viewModel = ViewModelProvider(this,factory)[MessagesViewModel::class.java]
        adapterMessages = AdapterMessages(viewModel.getCurrentUserid())
        textViewHeader = findViewById(R.id.textViewHeader)
        statusMes = findViewById(R.id.statusMes)
        recyclerMessages = findViewById(R.id.recyclerMessages)
        recyclerMessages.adapter = adapterMessages
        editTextMessage = findViewById(R.id.editTextMessage)
        imageViewSend = findViewById(R.id.imageViewSend)


    }

    companion object{
        fun newIntent(context: Context, user: User): Intent {
            val intent = Intent(context, MessagesActivity::class.java)
            intent.putExtra("user", user)
            return intent
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStatus(true)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setStatus(false)
    }
}