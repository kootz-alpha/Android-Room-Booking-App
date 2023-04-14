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

class SignInPage : AppCompatActivity() {

    lateinit var name: EditText
    lateinit var pass: EditText
    lateinit var user_id: EditText
    lateinit var get: Button
    lateinit var sharedPreferences: SharedPreferences

    lateinit var btnDatePicker: Button
    lateinit var txtDate: EditText
    private var mYear:Int = 0
    private var mMonth:Int = 0
    private var mDay:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_page)
        name = findViewById(R.id.etName)
        pass = findViewById(R.id.etPsw)
        user_id = findViewById(R.id.etUsrnm)

        sharedPreferences = getSharedPreferences("UserData" , Context.MODE_PRIVATE)

        btnDatePicker = findViewById(R.id.btn1)
        txtDate = findViewById(R.id.et1)


        btnDatePicker.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                this ,
                {view , year , monthOfYear , dayOfMonth
                    ->
                    txtDate.setText(
                        dayOfMonth.toString() + "-" +
                                (monthOfYear + 1) + "-" + year
                    )
                },
                mYear , mMonth , mDay
            )
            datePickerDialog.show()
        }
    }

    fun signin(v: View){

        val full_name = name.text.toString()
        val password = pass.text.toString()
        val user_name = user_id.text.toString()

        if (full_name != "" && password != "" && user_name != "") {

            sharedPreferences.edit().putString(user_name, "$password, $full_name").apply()
            Toast.makeText(this , "SignUp successful" , Toast.LENGTH_SHORT).show()

            var int = Intent(this, LoginPage::class.java)
            startActivity(int)
            finish()
        }
        else {
            Toast.makeText(this , "Fill the details carefully" , Toast.LENGTH_SHORT).show()
        }


    }
}