package com.example.main

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class DeleteSettings : AppCompatActivity() {

    lateinit var edtExistId: EditText
    lateinit var btnConfirmDelete: Button

    lateinit var dbManager: DBManager
    lateinit var sqlDB: SQLiteDatabase

    lateinit var dbManager2: DBManager2
    lateinit var sqliteDB: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_settings)

        dbManager = DBManager(this, "register", null, 1)
        dbManager2 = DBManager2(this, "list", null, 1)

        edtExistId = findViewById(R.id.edtExistId)
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete)

        //edittext에 있는 값을 이용하여 해당 id를 찾은 후 모든 데이터 삭제
        btnConfirmDelete.setOnClickListener {
            sqlDB = dbManager.writableDatabase
            sqliteDB = dbManager2.writableDatabase

            sqlDB.execSQL("DELETE FROM register WHERE id ='${edtExistId.text}';")
            sqliteDB.execSQL("DELETE FROM list WHERE id ='${edtExistId}';")

            Toast.makeText(this, "회원탈퇴가 되었습니다", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivtiy::class.java)
            startActivity(intent)

            sqlDB.close()
            sqliteDB.close()
        }
    }
}