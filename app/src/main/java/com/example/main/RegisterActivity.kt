package com.example.main

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar

class RegisterActivity : AppCompatActivity() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edtName: EditText
    lateinit var edtId: EditText
    lateinit var edtPw: EditText

    lateinit var btnRegister: Button
    lateinit var actionBar : ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister = findViewById(R.id.btnRegister)
        edtName = findViewById(R.id.edtName)
        edtId = findViewById(R.id.edtId)
        edtPw = findViewById(R.id.edtPw)

        actionBar = supportActionBar!!
        actionBar.hide()


        dbManager = DBManager(this, "registerDB", null, 1)

        btnRegister.setOnClickListener {
            var str_name: String = edtName.text.toString()
            var str_id: String = edtId.text.toString()
            var str_pw: String = edtPw.text.toString()

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO register VALUES ('"+str_name+"','"+str_id+"','"+str_pw+"')")
            sqlitedb.close()

            val intent = Intent(this, LoginActivtiy::class.java)
            startActivity(intent)
        }
    }
}