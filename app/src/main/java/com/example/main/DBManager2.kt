package com.example.main

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager2(
        context: Context?,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase?) {

        db!!.execSQL("CREATE TABLE list (id text, ques text, ans text, date text, logonoff text, emotion text, secret text, pic BLOB)")
         // registerDB의 id 변수를 담은 새 테이블 생성
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS list")
        onCreate(db)
    }
}