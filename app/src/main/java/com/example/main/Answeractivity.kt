package com.example.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.main.ui.home.HomeFragment
import java.lang.Exception

class Answeractivity : AppCompatActivity()
{
    lateinit var ques : TextView
    lateinit var camBtn : ImageButton

    lateinit var emoBtn1 : ImageButton
    lateinit var emoBtn2 : ImageButton
    lateinit var emoBtn3 : ImageButton
    lateinit var emoBtn4 : ImageButton

    lateinit var answer : EditText

    lateinit var photo : ImageView

    lateinit var dbManager2: DBManager2
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var date : String
    lateinit var emotion : String
    lateinit var secret : String
    lateinit var pic : String


    private fun permission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                var dlg = AlertDialog.Builder(this)
                dlg.setTitle("권한이 필요한 이유")
                dlg.setMessage("사진 정보를 얻기 위해서는 외부 저장소 권한이 필수로 필요합니다")
                dlg.setPositiveButton("확인"){dialog, which -> ActivityCompat.requestPermissions(this@Answeractivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1000)}
                dlg.setNegativeButton("취소", null)
                dlg.show()
            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1000)
            }
        }
        else {
            loadImage()
        }
    }


    val gallery = 0
    private fun loadImage(){

        val intent_cam = Intent(Intent.ACTION_PICK)
        intent_cam.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent_cam, gallery)
    }
    private fun emotion(emo: ImageButton){
        when(emo){
            emoBtn1 -> {
                //happy 데이터베이스에 저장
                emotion = "Happy"
                Toast.makeText(this, "Happy", Toast.LENGTH_SHORT).show()
            }
            emoBtn2 -> {
                //good 데이터베이스에 저장
                emotion = "Good"
                Toast.makeText(this, "Good", Toast.LENGTH_SHORT).show()
            }
            emoBtn3 -> {
                //soso 데이터베이스에 저장
                emotion = "Soso"
                Toast.makeText(this, "Soso", Toast.LENGTH_SHORT).show()
            }
            emoBtn4 -> {
                //bad 데이터베이스에 저장
                emotion = "Bad"
                Toast.makeText(this, "Bad", Toast.LENGTH_SHORT).show()
            }
            else -> {
            }
        }
    }
    private fun save(){
        // register db에서 찾아서
        // id랑 동일한거 찾은 후
        // 해당 id의 릴레이션들에 질문, 답변, 날짜 저장
        dbManager2 = DBManager2(this, "list", null, 1)

        sqlitedb = dbManager2.readableDatabase
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM list;", null)

        var on = "On"
        var idData = ""
        var onf = ""
        var q = ques.text.toString()
        var a = answer.text.toString()

        while (cursor.moveToNext()) {
            onf = cursor.getString(4)
            idData = cursor.getString(0)

        }
        if (onf == on) { // 로그인 상태라면
            Toast.makeText(this, "저장됨", Toast.LENGTH_SHORT).show()
            sqlitedb = dbManager2.writableDatabase
            //sqlitedb.execSQL("INSERT INTO list VALUES ('$idData', '$q', '$a', '$date', '$on', '$emotion', '$secret', '$pic')")

            sqlitedb.execSQL("UPDATE list SET ques='$q' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET ans='$a' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET date='$date' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET logonoff='$on' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET emotion='$emotion' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET secret='$secret' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET pic='$pic' WHERE id='$idData';")

            //id text, ques text, ans text, date text, logonoff text, emotion text, secret text, pic text
            sqlitedb.close()

            var intent = Intent(this, MainActivity::class.java)
           // intent.putExtra("count", 1)
            startActivity(intent)

        }
        else{

        }



        cursor.close()
        sqlitedb.close()
    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answeractivity)
        ques = findViewById(R.id.ques)
        camBtn = findViewById(R.id.cameraButton)
        emoBtn1 = findViewById(R.id.emotionButton1)
        emoBtn2 = findViewById(R.id.emotionButton2)
        emoBtn3 = findViewById(R.id.emotionButton3)
        emoBtn4 = findViewById(R.id.emotionButton4)
        photo = findViewById(R.id.photoview)
        answer = findViewById(R.id.answers)
        secret = "public"
        pic = "none"
        emotion = "none"


        val year = intent.getStringExtra("year")
        val month = intent.getStringExtra("month")
        val day = intent.getStringExtra("day")
        date = "$year" +"년 " + "$month" + "월 "+ "$day" + "일"

       // id를 intent로 받지 말고 login logoff 여부 체크해서 login 되어잇는 사람의 id 데베에서 끌어옴


        supportActionBar!!.title = "$year" +"년 " + "$month" + "월 "+ "$day" + "일의 일기"

        camBtn.setOnClickListener{
            permission()

        }

        if(intent.hasExtra("question")){
            ques.text= intent.getStringExtra("question")

        }else{
            // 질문 수정이 없었다면 설정된 질문 물어보기
            ques.text = "Q. 당신은 행복한가요? " // default

        }



        emoBtn1.setOnClickListener {
            emotion(emoBtn1)
            //db (아래 동일)
        }
        emoBtn2.setOnClickListener {
            emotion(emoBtn2)
        }
        emoBtn3.setOnClickListener {
            emotion(emoBtn3)
        }
        emoBtn4.setOnClickListener {
            emotion(emoBtn4)

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==gallery && resultCode == Activity.RESULT_OK){
            val selectedPhotoUri = data?.data
            photo.setImageURI(selectedPhotoUri)
            pic = selectedPhotoUri.toString()
        }
        else{
            Toast.makeText(this, "사진을 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.private_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ans_private -> {
                // 비공개
                secret = "private"
                true
            }
            R.id.ans_public -> {
                // 공개
                true
            }
            R.id.save_button -> {
                //저장
                //공개 저장이 디폴트
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    }



