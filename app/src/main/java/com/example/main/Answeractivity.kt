package com.example.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class Answeractivity : AppCompatActivity() {
    lateinit var ques : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answeractivity)
        ques = findViewById(R.id.ques)
        val intent = getIntent()

        if(intent.hasExtra("question")){
            ques.text= intent.getStringExtra("question")

        }else{
            // 질문 수정이 없었다면 설정된 질문 물어보기
            // 질문들은 리스트로 처리
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



