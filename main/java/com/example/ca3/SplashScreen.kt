package com.example.ca3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log

class SplashScreen : AppCompatActivity() {

    private var TIME_OUT = 5000L
    private val loginDefault = SharedPreferencesDefaultData().loginData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val loggedInSharedPreferences = getSharedPreferences("LoggedIn", Context.MODE_PRIVATE)

        if (!loggedInSharedPreferences.contains("login")) {

            loggedInSharedPreferences.edit().putString("login", loginDefault["login"]).apply()
        }

        val loggedInData = loggedInSharedPreferences.getString("login", "")!!.split(", ").toMutableList()

        Handler().postDelayed({

            val startAppIntent: Intent
            Log.d("SplashScreen", loggedInData[0])
            if (loggedInData[0].toBoolean()) {
                startAppIntent = Intent(this, HomePage::class.java)
                startAppIntent.putExtra("UserID", loggedInData[1])
            }
            else {
                startAppIntent = Intent(this, LoginPage::class.java)
            }

            startActivity(startAppIntent)
            finish()
        }, TIME_OUT)
    }
}