package com.example.firstproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val user = users.find { it.username == username && it.password == password }

            if (user != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("currentUserName", user.name)
                intent.putExtra("currentUserUsername", user.username)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Қате логин немесе құпиясөз", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
