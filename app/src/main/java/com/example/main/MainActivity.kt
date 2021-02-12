package com.example.main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.main.ui.calendar.CalendarFragment
import com.example.main.ui.friends.FriendsFragment
import com.example.main.ui.home.HomeFragment
import com.example.main.ui.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var ab: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ab = supportActionBar!!
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(HomeFragment())
        setTitle("홈")

        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.navigation_home-> {
                    setCurrentFragment(HomeFragment())
                    ab.setTitle("홈")
                }
                R.id.navigation_calendar -> {
                    setCurrentFragment(CalendarFragment())
                    ab.setTitle("모아보기")
                }
                R.id.navigation_friends -> {
                    setCurrentFragment(FriendsFragment())
                    ab.setTitle("친구")
                }
                R.id.navigation_settings -> {
                    setCurrentFragment(SettingsFragment())
                    ab.setTitle("설정")
                }
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