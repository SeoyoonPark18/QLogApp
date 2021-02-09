package com.example.main

import android.graphics.Color
import android.graphics.Color.blue
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.main.ui.home.HomeFragment
import com.google.android.material.internal.ContextUtils.getActivity

class Answeractivity : AppCompatActivity()
{
    lateinit var ques : TextView
    lateinit var camBtn : ImageButton
    lateinit var txtBtn : ImageButton
    lateinit var emoBtn : ImageButton


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answeractivity)
        ques = findViewById(R.id.ques)
       // camBtn = findViewById(R.id.cameraButton)
      //  txtBtn = findViewById(R.id.textButton)
       // emoBtn = findViewById(R.id.emotionButton)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(getColor(R.color.blue)))
        val intent = intent

        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val day = intent.getStringExtra("day")
        supportActionBar!!.title = "$year" +"년 " + "$month" + "월 "+ "$day" + "일의 일기"

        if(intent.hasExtra("question")){
            ques.text= intent.getStringExtra("question")

        }else{
            // 질문 수정이 없었다면 설정된 질문 물어보기
            // 처리 안 되는 중...
            ques.text = "Q. 당신은 행복한가요? " // default
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.private_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ans_private -> {
                // 해당 답변 잠금
                true
            }
            R.id.ans_public -> {
                // 해당 답변 오픈
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    }



