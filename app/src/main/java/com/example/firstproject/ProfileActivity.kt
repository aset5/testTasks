package com.example.firstproject

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        // Устанавливаем язык до super.onCreate
        prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("app_language", "ru") ?: "ru"
        setLocale(language)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Получаем имя пользователя и логин из Intent
        val name = intent.getStringExtra("currentUserName")
        val username = intent.getStringExtra("currentUserUsername")

        // Получаем накопленный общий балл из SharedPreferences
        val savedScore = prefs.getInt("total_score", 0)

        // Инициализируем TextView'ы
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val usernameTextView = findViewById<TextView>(R.id.usernameTextView)
        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)

        // Устанавливаем текст
        nameTextView.text = getString(R.string.name_label, name)
        usernameTextView.text = getString(R.string.username_label, username)
        scoreTextView.text = getString(R.string.score_label, savedScore)

        // Обработчики кнопок для смены языка
        findViewById<Button>(R.id.buttonLangRu).setOnClickListener {
            changeLanguage("ru")
        }
        findViewById<Button>(R.id.buttonLangKz).setOnClickListener {
            changeLanguage("kk")
        }
        findViewById<Button>(R.id.buttonLangEn).setOnClickListener {
            changeLanguage("en")
        }
    }

    private fun changeLanguage(langCode: String) {
        if (langCode != prefs.getString("app_language", "ru")) {
            prefs.edit().putString("app_language", langCode).apply()
            setLocale(langCode)
            recreate()
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}
