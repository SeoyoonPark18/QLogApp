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
import java.util.*


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

    lateinit var sqldb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionBar = supportActionBar!!
        actionBar.hide()

        edtLoginId = findViewById(R.id.edtLoginId)
        edtLoginPw = findViewById(R.id.edtLoginPw)
        btnLogin = findViewById(R.id.btnLogin)
        btnToRegister = findViewById(R.id.btnToRegister)

        dbManager = DBManager(this, "register", null, 1)
        dbManager2 = DBManager2(this, "list", null, 1)

        //회원가입 데이터 조회하여 로그인
        btnLogin.setOnClickListener {
            sqlitedb = dbManager.readableDatabase

            var cursor: Cursor

            cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)

            val cal = Calendar.getInstance()

            val year = cal.get(Calendar.YEAR).toString()
            val month = (cal.get(Calendar.MONTH)+1).toString()
            val day = cal.get(Calendar.DATE).toString()

            var date = "$year" + "년 " + "$month" + "월 " + "$day" + "일"

            var idData = ""
            var pwData = ""
            var add = false

            //이동한 cursor가 id, pw 정보와 동일하면 로그인 성공 & 홈 화면으로 이동
            while (cursor.moveToNext()) {
                idData = cursor.getString(1)
                pwData = cursor.getString(2)

                id = edtLoginId.text.toString()
                pw = edtLoginPw.text.toString()

                if (id == idData && pw == pwData) {
                    val intent = Intent(this, MainActivity::class.java)

                    var date2 = ""
                    //답변 정보 정장위한 list 데이터베이스 삽입
                    sqldb = dbManager2.writableDatabase
                    var licursor: Cursor = sqldb.rawQuery("SELECT * FROM list WHERE date != '$date';", null)
                    while (licursor.moveToNext()){
                        date2 = licursor.getString(licursor.getColumnIndex("date"))
                    }
                    if (date2 == null){
                        sqldb.execSQL("INSERT INTO list (id, ques, ans, date, logonoff, emotion, secret) VALUES ('$idData', 'null', 'null', '$date', 'On', 'null', 'null')")

                    } else {
                        sqldb.execSQL("UPDATE list SET logonoff = 'On' WHERE id=='$idData';")
                    }
                    sqldb.execSQL("UPDATE list SET logonoff = 'Off' WHERE id!='$idData';")
                    add = true
                    sqldb.close()
                    //intent.putExtra("id", idData)
                    startActivity(intent)
                }
            }

            //로그인 실패 시 경고문구
            if(add == false) {
                Toast.makeText(this, "아이디 또는 비밀번호가 맞지 않습니다", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
            sqlitedb.close()
        }

        // 회원가입 화면으로 이동
        btnToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}