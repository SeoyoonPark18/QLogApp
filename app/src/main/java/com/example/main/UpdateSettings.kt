package com.example.main

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateSettings : AppCompatActivity() {

    lateinit var edtExistId: EditText
    lateinit var edtNewName: EditText
    lateinit var btnConfirmName: Button
    lateinit var edtExistId2: EditText
    lateinit var edtNewPw: EditText
    lateinit var btnConfirmPw: Button

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_settings)

        dbManager = DBManager(this, "register", null, 1)

        edtExistId = findViewById(R.id.edtExistId)
        edtNewName = findViewById(R.id.edtNewName)
        btnConfirmName = findViewById(R.id.btnConfirmName)
        edtExistId2 = findViewById(R.id.edtExistId2)
        edtNewPw = findViewById(R.id.edtNewPw)
        btnConfirmPw = findViewById(R.id.btnConfirmPw)

        //아이디 확인 후, 이름을 변경
        btnConfirmName.setOnClickListener {
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("UPDATE register SET name = " + edtNewName.text +" WHERE id = '"
                    + edtExistId.text.toString() +"';")
            sqlDB.close()
            Toast.makeText(applicationContext, "수정됨", Toast.LENGTH_SHORT).show()
        }
        //아이디 확인 후, 비밀번호 변경
        btnConfirmPw.setOnClickListener {
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("UPDATE register SET pw = " + edtNewPw.text +" WHERE id = '"
                    + edtExistId2.text.toString() +"';")
            sqlDB.close()
            Toast.makeText(applicationContext, "수정됨", Toast.LENGTH_SHORT).show()
        }
    }
}