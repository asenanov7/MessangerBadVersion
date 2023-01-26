package com.example.solomessenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

class ChatsActivity : AppCompatActivity() {
    private lateinit var recyclerUsers: RecyclerView
    private lateinit var progressBarUsers: ProgressBar
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatsAdapter:ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        init()
        chatsAdapter.bridge = object : ChatsAdapter.OnClickListener{
            override fun click(user: User) {
                val intent = MessagesActivity.newIntent(this@ChatsActivity, user)
                startActivity(intent)
            }
        }
        chatViewModel.getUsersLD().observe(this) {
            chatsAdapter.setDataOnChatsAdapter(it)

        }
        chatViewModel.getUseOuted().observe(this){
            if (it){
               finish()
                startActivity(MainActivity.newIntent(this))
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        chatViewModel.signOut()
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        recyclerUsers = findViewById(R.id.recyclerUsers)
        progressBarUsers = findViewById(R.id.progressBarUsers)
        chatsAdapter = ChatsAdapter(this)
        recyclerUsers.adapter = chatsAdapter
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ChatsActivity::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        chatViewModel.setStatus(true)
    }

    override fun onPause() {
        super.onPause()
        chatViewModel.setStatus(false)
    }
}