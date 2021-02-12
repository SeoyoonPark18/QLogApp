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

        edtExistId = findViewById(R.id.edtExistId)
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete)

        btnConfirmDelete.setOnClickListener {
            sqlDB = dbManager.writableDatabase
            var id = ""
            var cursor: Cursor = sqlDB.rawQuery("SELECT * FROM register;", null)
            while (cursor.moveToNext()){
                id = cursor.getString(cursor.getColumnIndex("id"))
            }
            if (id == edtExistId.text.toString()) {
                dbManager2 = DBManager2(this, "list", null, 1)
                sqliteDB = dbManager2.writableDatabase
                sqlDB.execSQL("DELETE FROM register WHERE id = '" + edtExistId.text + "';")
                sqliteDB.execSQL("DELETE FROM list WHERE id ='"+ edtExistId.text+"';")

                sqliteDB.close()
                Toast.makeText(this, "회원탈퇴가 되었습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivtiy::class.java)
                startActivity(intent)
            } else
                Toast.makeText(this, "아이디를 잘못 입력하셨습니다. 다시 입력해주세요", Toast.LENGTH_SHORT).show()
            cursor.close()
            sqlDB.close()
        }
    }
}