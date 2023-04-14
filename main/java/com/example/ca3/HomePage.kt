package com.example.ca3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import java.util.*


class HomePage : AppCompatActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var sharedPrefs: SharedPreferences
    private val monthsDict = SharedPreferencesDefaultData().monthsDict
    private val sharedPreferencesDefaultData = SharedPreferencesDefaultData().roomsData
    private val keyList = sharedPreferencesDefaultData.keys.toList()
    private var selectedRooms = mutableMapOf<String, Int>()
    private var userData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        sharedPrefs = getSharedPreferences("RoomsData", Context.MODE_PRIVATE)

        var textViews = mutableMapOf<String, TextView>()
        val addSwitch = findViewById<Switch>(R.id.addSwitch)
        val rmNum = findViewById<TextView>(R.id.txtRoomNum)
        val rmType = findViewById<TextView>(R.id.txtRoomType)
        val startDate = findViewById<TextView>(R.id.txt_start_date)
        val endDate = findViewById<TextView>(R.id.txt_end_date)
        val bottomDisplay = findViewById<ConstraintLayout>(R.id.bottom_display)
        val gridLayout = findViewById<GridLayout>(R.id.grid_layout)
        val navDrawerUsername = findViewById<TextView>(R.id.nav_drawer_username)

        val user_id = intent.getStringExtra("UserID")
        userData = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString(user_id, "password, Abhishek Haridasan")!!.split(", ").toMutableList()

        navDrawerUsername.text = userData[1]

        bottomDisplay.visibility = ViewGroup.INVISIBLE

        var index = 0

        for (row in 0 until gridLayout.rowCount) {
            for (column in 0 until gridLayout.columnCount) {


                if (index < keyList.size) {

                    val key = keyList[index]

                    if (!sharedPrefs.contains(key)) {
                        print("$key not found")
                        val editor = sharedPrefs.edit()
                        editor.putString(key, sharedPreferencesDefaultData[key])
                        editor.apply()
                    }

                    val layoutParams = GridLayout.LayoutParams().apply {
                        width = GridLayout.LayoutParams.WRAP_CONTENT
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        setGravity(Gravity.CENTER)
                        columnSpec = GridLayout.spec(column, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                    }
                    layoutParams.setMargins(10, 10, 10, 10)

                    val roomNum = key
                    val roomData = sharedPrefs.getString(key, "")!!.split(", ")
                    val roomType = roomData[1]
                    val roomBooked = roomData[3].toBoolean()

                    val textView = TextView(this, null)
                    textView.text = roomNum
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
                    textView.gravity = Gravity.CENTER
                    textView.setTextColor(Color.BLACK)
                    textView.setTypeface(null, Typeface.BOLD)
                    textViews[key] = textView

                    if (roomBooked) {

                        textView.setBackgroundResource(R.drawable.bg_red_border)

                        textView.setOnClickListener {

                            bottomDisplay.setBackgroundResource(R.drawable.bg_unavailable)
                            bottomDisplay.visibility = ViewGroup.VISIBLE

                            val calendar = Calendar.getInstance()
                            var year = calendar.get(Calendar.YEAR)
                            var month = monthsDict[calendar.get(Calendar.MONTH) + 1]
                            var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                            var date = "$dayOfMonth-$month-$year"
                            startDate.text = date

                            calendar.add(Calendar.DAY_OF_YEAR, 1)

                            year = calendar.get(Calendar.YEAR)
                            month = monthsDict[calendar.get(Calendar.MONTH) + 1]
                            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                            date = "$dayOfMonth-$month-$year"
                            endDate.text = date

                            rmNum.text = roomNum
                            rmType.text = roomType

                            addSwitch.isEnabled = false
                            addSwitch.isChecked = false
                        }
                    }
                    else {

                        textView.setBackgroundResource(R.drawable.bg_green_border)

                        textView.setOnClickListener {

                            bottomDisplay.setBackgroundResource(R.drawable.bg_available)
                            bottomDisplay.visibility = ViewGroup.VISIBLE

                            val calendar = Calendar.getInstance()
                            var year = calendar.get(Calendar.YEAR)
                            var month = monthsDict[calendar.get(Calendar.MONTH) + 1]
                            var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                            var date = "$dayOfMonth-$month-$year"
                            startDate.text = date

                            calendar.add(Calendar.DAY_OF_YEAR, 1)

                            year = calendar.get(Calendar.YEAR)
                            month = monthsDict[calendar.get(Calendar.MONTH) + 1]
                            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                            date = "$dayOfMonth-$month-$year"
                            endDate.text = date

                            rmNum.text = roomNum
                            rmType.text = roomType

                            addSwitch.isEnabled = true

                        }
                    }

                    gridLayout.addView(textView, layoutParams)
                }
                index += 1
            }
        }

        addSwitch.setOnCheckedChangeListener { _, isChecked ->

            val key = rmNum.text.toString()
            val textView = textViews[key]

            if (isChecked) {

                textView?.setBackgroundResource(R.drawable.bg_selected_cell)
                selectedRooms[key] = 1
            }
            else if (addSwitch.isEnabled){

                textView?.setBackgroundResource(R.drawable.bg_green_border)

                if (selectedRooms.containsKey(key)) {
                    selectedRooms.remove(key)
                }
            }

            Log.d("", selectedRooms.keys.joinToString(" "))
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

        if (item.itemId == R.id.btn_cart) {

            val intent = Intent(this, CheckoutPage::class.java)
            intent.putExtra("roomsList", selectedRooms.keys.toList().joinToString(", "))
            intent.putExtra("UserID", userData[1])

            startActivity(intent)
        }
        else if (item.itemId == R.id.btn_sign_out) {

            val loggedInSharedPreferences = getSharedPreferences("LoggedIn", Context.MODE_PRIVATE)
            val loggedInList = loggedInSharedPreferences.getString("login", "")!!.split(", ").toMutableList()
            loggedInList[0] = "false"

            Log.d("HomePage", loggedInList.joinToString(", "))

            loggedInSharedPreferences.edit().putString("login", loggedInList.joinToString(", ")).apply()
            val restartIntent = Intent(this, SplashScreen::class.java)

            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(restartIntent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}