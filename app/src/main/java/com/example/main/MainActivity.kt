package com.example.main


import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
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

    lateinit var DBManager2: DBManager2
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ab = supportActionBar!!
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(HomeFragment())
        setTitle("홈")

        //메인 화면 아래의 네비게이션 바
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

        //현재 로그인한 사람의 아이디를 제외하고 로그인 상태를 off로 바꿈
        var id = intent.getStringExtra("id")
        DBManager2 = DBManager2(this, "list", null, 1)
        sqlDB = DBManager2.writableDatabase

        sqlDB.execSQL("UPDATE list SET logonoff = 'Off' WHERE id != '$id';")

        sqlDB.close()
    }

    private fun setCurrentFragment(fragment: Fragment)=
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.flFragment,fragment)
                commit()
            }
}