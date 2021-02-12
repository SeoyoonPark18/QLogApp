package com.example.main

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import java.io.ByteArrayOutputStream
import java.lang.Exception

class Answeractivity : AppCompatActivity() {
    lateinit var ques: TextView // 질문
    lateinit var camBtn: ImageButton // 사진

    lateinit var emoBtn1: ImageButton // 감정 버튼
    lateinit var emoBtn2: ImageButton
    lateinit var emoBtn3: ImageButton
    lateinit var emoBtn4: ImageButton

    lateinit var answer: EditText // 답변 버튼

    lateinit var photo: ImageView // 사진을 출력할 뷰

    lateinit var dbManager2: DBManager2 // 데이터베이스 매니저
    lateinit var dateDBManager: dateDBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var date: String // 날짜, 감정, 공개/비공개, 사진 변수
    lateinit var emotion: String
    lateinit var secret: String
    lateinit var picbyte: ByteArray

    lateinit var year: String
    lateinit var month: String
    lateinit var day : String

    private fun permission(){ // 사진 버튼을 클릭했을 시 권한 여부
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
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
        else { // 권한이 잘 적용 되었다면
            loadImage() // 사진 불러오기
        }
    }


    val gallery = 0
    private fun loadImage(){

        val intent_cam = Intent(Intent.ACTION_PICK)
        intent_cam.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intent_cam, gallery) // 외부저장소로부터 사진 받아옴
    }

    private fun emotion(emo: ImageButton) {
        when (emo) { // 누른 감정 버튼에 따라 감정 변수의 string 지정
            emoBtn1 -> {
                emotion = "Happy"
                Toast.makeText(this, "Happy", Toast.LENGTH_SHORT).show()
            }
            emoBtn2 -> {
                emotion = "Good"
                Toast.makeText(this, "Good", Toast.LENGTH_SHORT).show()
            }
            emoBtn3 -> {
                emotion = "Soso"
                Toast.makeText(this, "Soso", Toast.LENGTH_SHORT).show()
            }
            emoBtn4 -> {
                emotion = "Bad"
                Toast.makeText(this, "Bad", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun save() {
        // register db에서 찾아서
        // 해당 id와 동일한 튜플 찾은 후 질문, 답변, 날짜 저장
        dbManager2 = DBManager2(this, "list", null, 1)

        sqlitedb = dbManager2.readableDatabase
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM list;", null)

        var on = "On"
        var idData = ""
        var onf = ""

        while (cursor.moveToNext()) {
            onf = cursor.getString(4)
            idData = cursor.getString(0)
        }

        if (onf == on) { // 로그인 상태인 회원 찾아 값 저장
            Toast.makeText(this, "저장됨", Toast.LENGTH_SHORT).show()

            dateDBManager = dateDBManager(this, "dateDB", null, 1)
            sqlitedb = dbManager2.writableDatabase
            var dateSQL : SQLiteDatabase = dateDBManager.readableDatabase

            sqlitedb.execSQL("UPDATE list SET ques='${ques.text}' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET ans='${answer.text}' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET date='$date' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET logonoff='$on' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET emotion='$emotion' WHERE id='$idData';")
            sqlitedb.execSQL("UPDATE list SET secret='$secret' WHERE id='$idData';")
            var p: SQLiteStatement = sqlitedb.compileStatement("UPDATE list SET pic = ? WHERE id=?;")
            p.bindBlob(1, picbyte)
            p.bindString(2, idData)
            p.execute()

            dateSQL.execSQL("INSERT INTO dateDB VALUES ('$idData', '$date','$year', '$month', '$day')")
            dateSQL.close()

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

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
        picbyte = byteArrayOf(2)
        emotion = "none"


        year = intent.getStringExtra("year").toString()
        month = intent.getStringExtra("month").toString()
        day = intent.getStringExtra("day").toString()

        date = "$year" + "년 " + "$month" + "월 " + "$day" + "일"

        supportActionBar!!.title = "$year" +"년 " + "$month" + "월 "+ "$day" + "일의 일기" // 타이틀바 수정

        camBtn.setOnClickListener{
            permission() // 카메라 버튼 클릭시 권한 여부 우선 체크

        }

        if(intent.hasExtra("question")){ // 변경된 질문이 있다면
            ques.text= intent.getStringExtra("question") // 변경된 값으로 질문 저장

        }else{
            // 질문 수정이 없었다면 디폴트 질문 물어보기
            ques.text = "Q. 당신은 행복한가요? " // default

        }


        // 감정 버튼 클릭 시
        emoBtn1.setOnClickListener {
            emotion(emoBtn1)
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

        // 사진 불러오기
        if(requestCode==gallery && resultCode == Activity.RESULT_OK){
            val selectedPhotoUri = data?.data
            photo.setImageURI(selectedPhotoUri)
            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(photo.context.contentResolver, selectedPhotoUri)
            var stream = ByteArrayOutputStream()
            var width = 350
            var height = bitmap.height*350/bitmap.width
            if (bitmap.width > 400) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width, height,true)
                bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream)
            }
            picbyte = stream.toByteArray()
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



