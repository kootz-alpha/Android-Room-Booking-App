package com.example.ca3

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import java.util.*

class CreditCardActivity : AppCompatActivity() {
    lateinit var txtDate : EditText
    private var mYear : Int = 0
    private var mMonth : Int = 0
    private var mDay : Int = 0
    private lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var paybtnn :Button
    private var userData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit_card)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navDrawerUsername = findViewById<TextView>(R.id.nav_drawer_username)

        val user_id = intent.getStringExtra("UserID")
        userData = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString(user_id, "password, Abhishek Haridasan")!!.split(", ").toMutableList()

        navDrawerUsername.text = userData[1]

        txtDate = findViewById(R.id.date)
        paybtnn = findViewById(R.id.paybtn)
        var roomsList = intent.getStringExtra("RoomsList")!!.split(", ")
        var totalText = findViewById<TextView>(R.id.creditcardTotal)
        var sharedPrefs = getSharedPreferences("RoomsData", Context.MODE_PRIVATE)
        var totalPrice = intent.getIntExtra("TotalPrice", 0).toString()

        totalText.text = "Total: Rs.$totalPrice"

        txtDate.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth
                    ->
                    txtDate.setText(""+(monthOfYear + 1) + "/" + year)
                },
                mYear, mMonth, mDay
            )
            datePickerDialog.show()
        }

        paybtnn.setOnClickListener{

            var credit_name = findViewById<EditText>(R.id.name)
            var credit_num = findViewById<EditText>(R.id.cardnum)
            var credit_date = findViewById<EditText>(R.id.date)
            var credit_cvv = findViewById<EditText>(R.id.cvv)

            if (credit_cvv.text.toString() != "" && credit_name.text.toString() != "" && credit_num.text.toString() != "" && credit_date.text.toString() != "") {


                for (key in roomsList) {

                    val creditPageRoomData = sharedPrefs.getString(key, "")!!.split(", ").toMutableList()
                    creditPageRoomData[3] = "true"

                    sharedPrefs.edit().putString(key, creditPageRoomData.joinToString(", ")).apply()
                }

                finish()
            }
            else {

                Toast.makeText(this, "Fill every details carefully", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.action_bar, menu)
        title = ""
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        if (item.itemId == R.id.btn_sign_out) {

            val loggedInSharedPreferences = getSharedPreferences("LoggedIn", Context.MODE_PRIVATE)
            val loggedInList = loggedInSharedPreferences.getString("login", "true, 123")!!.split(", ").toMutableList()
            loggedInList[1] = "false"

            loggedInSharedPreferences.edit().putString("login", loggedInList.joinToString(", ")).apply()
            val restartIntent = Intent(this, SplashScreen::class.java)

            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(restartIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}