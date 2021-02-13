package com.example.main

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar

class RegisterActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edtName: EditText
    lateinit var edtId: EditText
    lateinit var edtPw: EditText

    lateinit var btnRegister: Button
    lateinit var actionBar: ActionBar

    lateinit var dbManager2: DBManager2
    lateinit var sqldb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        actionBar = supportActionBar!!
        actionBar.hide()

        btnRegister = findViewById(R.id.btnRegister)
        edtName = findViewById(R.id.edtName)
        edtId = findViewById(R.id.edtId)
        edtPw = findViewById(R.id.edtPw)

        dbManager = DBManager(this, "register", null, 1)

        //회원가입 데이터 저장 (이름,아이디,비밀번호)
        btnRegister.setOnClickListener {
            var str_name: String = edtName.text.toString()
            var str_id: String = edtId.text.toString()
            var str_pw: String = edtPw.text.toString()

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO register VALUES ('"+str_name+"','"+str_id+"','"+str_pw+"')")
            dbManager2 = DBManager2(this, "list", null, 1)
            sqldb = dbManager2.writableDatabase
            sqldb.execSQL("INSERT INTO list (id, ques, ans, date, logonoff, emotion, secret) VALUES ('$str_id', 'null', 'null', 'null', 'null', 'null', 'null')")
            sqlitedb.close()
            sqldb.close()

            val intent = Intent(this, LoginActivtiy::class.java)
            Toast.makeText(this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}