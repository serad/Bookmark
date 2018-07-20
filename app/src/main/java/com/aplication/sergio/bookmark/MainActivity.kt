package com.aplication.sergio.bookmark


import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.widget.Toast
import com.aplication.sergio.bookmark.fragments.HomeFragment
import com.aplication.sergio.bookmark.fragments.ListFragment
import com.aplication.sergio.bookmark.fragments.SettingsFragment
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.preference.PreferenceManager
import android.support.v7.widget.PopupMenu
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.aplication.sergio.bookmark.helper.SQLHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdListener
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    val searchFragment get() = SEARCH_FRAGMENT
    companion object {

        const val SEARCH_FRAGMENT = 100
        fun newIntent(context: Context ): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Inflate the layout for this fragment
       // MobileAds.initialize(this, "ca-app-pub-6415490707456444~4575057491")
        //mInterstitialAd = InterstitialAd(this)
      //  mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"


        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        MobileAds.initialize(this, "ca-app-pub-6415490707456444~4575057491")
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-6415490707456444/9224748948"
        val request = AdRequest.Builder()
                .addTestDevice("B1F0294E768ACD86E78D1957B37F712D")  // An example device ID
                .build()
        mInterstitialAd.loadAd(request)




        val homeFragment = HomeFragment.newInstance()
        openFragment(homeFragment)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                // Action when tab 1 selected
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                Toast.makeText(this@MainActivity, "home", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {

                if (mInterstitialAd.isLoaded) {
                     mInterstitialAd.show()

                } else {
                    val request = AdRequest.Builder()
                            .addTestDevice("B1F0294E768ACD86E78D1957B37F712D")  // An example device ID
                            .build()
                    mInterstitialAd.loadAd(request)
                }
                val connManager: ConnectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val netInfo: NetworkInfo? = connManager.activeNetworkInfo

                if(netInfo != null && netInfo.isConnected ){

                    val res: Resources = resources
                    val genderList = res.getStringArray(R.array.gendersArray)
                    val dbHandler = SQLHelper(this, null, null, 1)
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user!!.uid
                    val userGendersArray = dbHandler.listUserPreferences(uid)

                    val randomGenerator = Random()
                    var ramdomGender = genderList[(randomGenerator.nextInt(genderList!!.size))]
                    if( userGendersArray!!.size > 0 )
                    {
                        ramdomGender = genderList[ userGendersArray.get( (randomGenerator.nextInt(userGendersArray!!.size) ) ).userPreferenceId ]
                        Log.i("GEDER", ramdomGender.toString())
                    }
                    val gender = ramdomGender
                    val queryString = "subject:$gender"
                    Log.i("GEDER", queryString)
                    try {
                        FetchBook(this ,SEARCH_FRAGMENT).execute( queryString )
                    } catch (e: Exception) {
                        FetchBook(this ,SEARCH_FRAGMENT).execute( queryString )
                    }
                } else {
                    Toast.makeText( this, "Please check our connection", Toast.LENGTH_LONG).show()
                }

                Toast.makeText(this@MainActivity, "search", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_list -> {
                val listFragment = ListFragment.newInstance()
                openFragment(listFragment)
                // Action when tab 2 selected
                Toast.makeText(this@MainActivity, "list", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_setting -> {
                val settingsFragment = SettingsFragment.newInstance()
                openFragment(settingsFragment)
                // Action when tab 2 selected
                Toast.makeText(this@MainActivity, "setting", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
            bottomNavigation.visibility = View.GONE
            val imgButton: ImageButton = findViewById(R.id.imageButton2)
            imgButton.visibility = View.VISIBLE
        } else {
            val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
            bottomNavigation.visibility = View.VISIBLE
            val imgButton: ImageButton = findViewById(R.id.imageButton2)
            imgButton.visibility = View.GONE
        }
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.navigation, popup.menu)
        popup.show()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }
}
