package com.example.firstproject
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userName = intent.getStringExtra("USER_NAME")
        findViewById<TextView>(R.id.textWelcome).text = "Қош келдіңіз, $userName!"

        findViewById<Button>(R.id.buttonLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        val logicGamesButton = findViewById<Button>(R.id.buttonLogicGames)
        logicGamesButton.setOnClickListener {
            val intent = Intent(this, LogicalGamesActivity::class.java)
            startActivity(intent)
        }
        val currentUserName = intent.getStringExtra("currentUserName")
        val currentUserUsername = intent.getStringExtra("currentUserUsername")

        val profileButton = findViewById<Button>(R.id.buttonProfile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("currentUserName", currentUserName)
            intent.putExtra("currentUserUsername", currentUserUsername)
            startActivity(intent)
        }
        val buttonTasks = findViewById<Button>(R.id.buttonTasks)
        buttonTasks.setOnClickListener {
            val intent = Intent(this, TasksActivity::class.java)
            startActivity(intent)
        }
        val riddlesButton = findViewById<Button>(R.id.buttonRiddles)
        riddlesButton.setOnClickListener {
            val intent = Intent(this, RiddlesActivity::class.java)
            startActivity(intent)
        }



    }

}

