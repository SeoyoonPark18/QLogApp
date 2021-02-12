package com.example.main

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar


class LoginActivtiy : AppCompatActivity() {

    lateinit var edtLoginId: EditText
    lateinit var edtLoginPw: EditText
    lateinit var btnLogin: Button
    lateinit var btnToRegister: Button

    lateinit var actionBar: ActionBar

    lateinit var id: String
    lateinit var pw: String

    lateinit var dbManager: DBManager

    lateinit var dbManager2: DBManager2
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar = supportActionBar!!
        actionBar.hide()

        edtLoginId = findViewById(R.id.edtLoginId)
        edtLoginPw = findViewById(R.id.edtLoginPw)
        btnLogin = findViewById(R.id.btnLogin)
        btnToRegister = findViewById(R.id.btnToRegister)

        dbManager = DBManager(this, "registerDB", null, 1)

        btnLogin.setOnClickListener {
            sqlitedb = dbManager.readableDatabase

            var cursor: Cursor

            cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)

            var idData = ""
            var pwData = ""
            var on = "On"
            var add = false

            while (cursor.moveToNext()) {
                idData = cursor.getString(1)
                pwData = cursor.getString(2)

                id = edtLoginId.text.toString()
                pw = edtLoginPw.text.toString()

                if (id == idData && pw == pwData) {
                    val intent = Intent(this, MainActivity::class.java)
                    dbManager2 = DBManager2(this, "list", null, 1)
                    sqlitedb = dbManager2.writableDatabase

                    sqlitedb.execSQL("INSERT INTO list VALUES ('$idData', 'null', 'null', 'null', '$on', 'null', 'null', 'none')")
                    //id text, ques text, ans text, date text, logonoff text, emotion text, secret text
                    add = true
                    startActivity(intent)
                }
            }

            if(add == false){
                Toast.makeText(this, "아이디 또는 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
            sqlitedb.close()
        }

        btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}