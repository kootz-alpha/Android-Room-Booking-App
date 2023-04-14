package com.example.ca3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class LoginPage : AppCompatActivity() {

    lateinit var name:EditText
    lateinit var pass: EditText
    lateinit var sharedPreferences: SharedPreferences
    private var userDataSharedPreferences = SharedPreferencesDefaultData().userData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)


        name = findViewById(R.id.etName)
        pass = findViewById(R.id.etPass)

        sharedPreferences = getSharedPreferences("UserData" , Context.MODE_PRIVATE)

        for (key in userDataSharedPreferences.keys) {

            if (!sharedPreferences.contains(key)) {

                sharedPreferences.edit().putString(key, userDataSharedPreferences[key]).apply()
            }
        }
    }

    fun signIn(v:View){

        var int = Intent(this, SignInPage::class.java)
        startActivity(int)
    }

    fun login(v:View){
        var int = Intent(this,HomePage::class.java)

        val user_id = name.text.toString()
        val password = pass.text.toString()

        if (!sharedPreferences.contains(user_id)) {
            Toast.makeText(this, "Incorrect Id or Password", Toast.LENGTH_SHORT).show()
        }
        else {

            val user_data = sharedPreferences.getString(user_id, "")!!.split(", ").toMutableList()

            if(password == user_data[0]){

                val loggedInSharedPreferences = getSharedPreferences("LoggedIn", Context.MODE_PRIVATE)
                val loggedInData = loggedInSharedPreferences.getString("login", "")!!.split(", ").toMutableList()
                loggedInData[0] = "true"

                loggedInSharedPreferences.edit().putString("login", loggedInData.joinToString(", ")).apply()

                int.putExtra("UserID", user_id)
                startActivity(int)
                finish()
            }
            else {
                Toast.makeText(this, "Incorrect Id or Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}