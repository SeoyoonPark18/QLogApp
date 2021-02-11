package com.example.main

import android.content.Intent
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_settings)

        dbManager = DBManager(this, "register", null, 1)

        edtExistId = findViewById(R.id.edtExistId)
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete)

        btnConfirmDelete.setOnClickListener {
            sqlDB = dbManager.writableDatabase
            sqlDB.execSQL("DELETE FROM register WHERE id = '" + edtExistId.text.toString() +"';")
            sqlDB.close()

            val intent = Intent(this, LoginActivtiy::class.java)
            startActivity(intent)
        }
    }
}