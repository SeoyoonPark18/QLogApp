package com.example.main

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UpdateSettings : AppCompatActivity() {

    lateinit var edtExistId: EditText
    lateinit var edtNewId: EditText
    lateinit var btnConfirmId: Button

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_settings)

        dbManager = DBManager(this, "registerDB", null, 1)

        edtExistId = findViewById(R.id.edtExistId)
        edtNewId = findViewById(R.id.edtNewId)
        btnConfirmId = findViewById(R.id.btnConfirmId)

        btnConfirmId.setOnClickListener {
            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("UPDATE registerDB SET id = " + edtNewId.text +" WHERE id = '"
                    + edtExistId.text.toString() +"';")

            sqlitedb.close()
            Toast.makeText(applicationContext, "수정됨", Toast.LENGTH_SHORT).show()
        }
    }
}