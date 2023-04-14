package com.example.ca3

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout

class CheckoutPage : AppCompatActivity() {

    lateinit var contibtn : Button
    lateinit var Card : RadioButton
    lateinit var Upi : RadioButton
    lateinit var Gcard : RadioButton
    lateinit var Radio : RadioGroup
    private var singleCost = 1150
    private var doubleCost = 3270
    private var suiteCost = 10000
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var userData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_page)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        contibtn = findViewById(R.id.Continue)
        Card = findViewById(R.id.card)
        Upi = findViewById(R.id.upi)
        Gcard = findViewById(R.id.gcard)
        Radio = findViewById(R.id.rdgroup)

        val navDrawerUsername = findViewById<TextView>(R.id.nav_drawer_username)

        val user_id = intent.getStringExtra("UserID")
        userData = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString(user_id, "password, Abhishek Haridasan")!!.split(", ").toMutableList()

        navDrawerUsername.text = userData[1]


        var roomsList = intent.getStringExtra("roomsList")!!.split(", ")
        var layout = findViewById<LinearLayout>(R.id.checkoutLinear)
        var sharedPrefs = getSharedPreferences("RoomsData", Context.MODE_PRIVATE)
        var totalPrice: Int = 0

        if (!roomsList.isEmpty()) {
            for (key in roomsList) {

                if (sharedPrefs.contains(key)) {

                    val checkoutPageRoomData = sharedPrefs.getString(key, "")!!.split(", ")
                    val checkoutPageRoomType = checkoutPageRoomData[1]
                    val price: String

                    if (checkoutPageRoomType == "Single") {
                        price = singleCost.toString()
                        totalPrice += singleCost
                    }
                    else if (checkoutPageRoomType == "Double") {
                        price = doubleCost.toString()
                        totalPrice += doubleCost
                    }
                    else {
                        price = suiteCost.toString()
                        totalPrice += suiteCost
                    }


                    var checkoutPage_text = TextView(this)
                    checkoutPage_text.text = "$key - $checkoutPageRoomType Room - Rs.$price"
                    checkoutPage_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    checkoutPage_text.setTextColor(Color.BLACK)

                    val checkoutPageParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    checkoutPageParams.setMargins(10, 0, 0, 10)

                    layout.addView(checkoutPage_text, checkoutPageParams)
                }
            }

            var checkoutPage_text = TextView(this)
            checkoutPage_text.text = "Total price: Rs.$totalPrice"
            checkoutPage_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            checkoutPage_text.setTextColor(Color.BLACK)
            checkoutPage_text.setTypeface(null, Typeface.BOLD)

            val checkoutPageParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            checkoutPageParams.setMargins(10, 10, 0, 10)

            layout.addView(checkoutPage_text, checkoutPageParams)
        }

        contibtn.setOnClickListener {
            val intSelectButton: Int = Radio.checkedRadioButtonId

            if(intSelectButton == R.id.card) {
                val intent = Intent(this, CreditCardActivity::class.java)
                intent.putExtra("TotalPrice", totalPrice)
                intent.putExtra("RoomsList", roomsList.joinToString(", "))
                intent.putExtra("UserID", userData[1])
                startActivity(intent)
            }
            else if(intSelectButton == R.id.upi) {
                val intent = Intent(this, UPI_Activity::class.java)
                intent.putExtra("TotalPrice", totalPrice)
                intent.putExtra("RoomsList", roomsList.joinToString(", "))
                intent.putExtra("UserID", userData[1])
                startActivity(intent)
            }
            else {
                Toast.makeText(this, "Choose any option", Toast.LENGTH_SHORT).show()
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