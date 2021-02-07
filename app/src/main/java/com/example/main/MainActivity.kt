package com.example.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.main.ui.calendar.CalendarFragment
import com.example.main.ui.friends.FriendsFragment
import com.example.main.ui.home.HomeFragment
import com.example.main.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        setCurrentFragment(HomeFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_home-> setCurrentFragment(HomeFragment())
                R.id.navigation_calendar -> setCurrentFragment(CalendarFragment())
                R.id.navigation_friends -> setCurrentFragment(FriendsFragment())
                R.id.navigation_settings -> setCurrentFragment(SettingsFragment())
            }
            true
        }

    }
    private fun setCurrentFragment(fragment: Fragment)=
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment,fragment)
                commit()
            }
}