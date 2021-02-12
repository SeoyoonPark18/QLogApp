package com.example.main.ui.friends

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.drm.DrmStore
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.*


class FriendsFragment : Fragment() {

    private lateinit var friendsViewModel: FriendsViewModel
    lateinit var layout: LinearLayout

    companion object {
        lateinit var friend_id: EditText
    }

    lateinit var id: String
    lateinit var name: String

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        friendsViewModel =
            ViewModelProvider(this).get(FriendsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        val sub = inflater.inflate(R.layout.dialog, container, false)
        setHasOptionsMenu(true)
        friend_id = sub.findViewById(R.id.friend_id)
        layout = root.findViewById(R.id.friend)
        show_friend()
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.friend_menu, menu)
    }

    // 메뉴 옵션
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.friend_add -> {
                dialog("친구 추가", "추가할 친구의 아이디를 입력해주세요.")
                return true
            }
            R.id.friend_del -> {
                dialog("친구 삭제", "삭제할 친구의 이름을 입력해주세요.")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 친구의 id를 입력 받고 존재하는 id라면 추가, 없는 id라면 추가 x
    fun check_id_add() {
        dbManager = DBManager(activity, "registerDB", null, 1)

        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)

        var idData: String = ""
        var nameData: String = ""
        var add = false

        while (cursor.moveToNext()) {
            idData = cursor.getString(1)

            id = friend_id.text.toString()


            if (id == idData) {
                nameData = cursor.getString(0)
                dbManager = DBManager(activity, "friendDB", null, 1)

                //friendDB에 추가한 친구 이름 넣기
                sqlitedb = dbManager.writableDatabase
                sqlitedb.execSQL("INSERT INTO register VALUES ('$nameData','$id','null')")
                sqlitedb.close()
                layout.removeAllViews()
                show_friend()
                Toast.makeText(activity, "$nameData 님이 추가되었습니다.", Toast.LENGTH_SHORT).show()
                add = true
            }
        }
        if (add == false) {
            Toast.makeText(activity, "회원정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        sqlitedb.close()
    }

    fun check_id_del() {
        dbManager = DBManager(activity, "registerDB", null, 1)

        sqlitedb = dbManager.readableDatabase

        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)

        var name: String = ""
        var nameData: String = ""
        var delete = false

        while (cursor.moveToNext()) {
            nameData = cursor.getString(0)

            id = friend_id.text.toString()

            if (id == nameData) {
                name = cursor.getString(0)
                dbManager = DBManager(activity, "friendDB", null, 1)

                //friendDB에 추가했던 친구 이름 삭제하기
                sqlitedb = dbManager.writableDatabase

                sqlitedb.execSQL("DELETE FROM register WHERE name = '$nameData';")
                sqlitedb.close()
                layout.removeAllViews()
                show_friend()
                Toast.makeText(activity, "$nameData 님이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                delete = true
            }
        }
        if (delete == false) {
            Toast.makeText(activity, "회원정보가 없습니다.", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        sqlitedb.close()
    }

    // 추가된 친구 목록 보여주는 함수
    fun show_friend() {
        dbManager = DBManager(activity, "friendDB", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)
        var num: Int = 0

        while (cursor.moveToNext()) {
            var nameData = cursor.getString(0)
            //id = cursor.getString(1)

            var layout_item: LinearLayout = LinearLayout(activity)
            layout_item.orientation = LinearLayout.HORIZONTAL
            layout_item.id = num


            var tvImageView: ImageView = ImageView(activity)
            tvImageView.setImageResource(R.drawable.ic_baseline_face_24)
            tvImageView.setPadding(0,5,0,0)
            tvImageView.baselineAlignBottom
            tvImageView.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout_item.addView(tvImageView)


            var tvName: TextView = TextView(activity)
            tvName.text = nameData
            tvName.textSize = 25f
            tvName.setPadding(10,0,0,0)
            tvName.setTextColor(Color.GRAY)
            layout_item.addView(tvName)


            layout_item.setOnClickListener {
                var change_id = layout_item.id
                id = change_id(change_id)
                val intent = Intent(getActivity(), Friend_Activity::class.java)
                intent.putExtra("intent_id", id)
                startActivity(intent)
            }
            layout.addView(layout_item)
            num++
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()

    }

    // 친구가 선택됐을 때 친구의 id 값을 얻어오는 함수
    fun change_id(int: Int): String {
        dbManager = DBManager(activity, "friendDB", null, 1)
        sqlitedb = dbManager.readableDatabase
        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)
        var count: Int = 0
        var s_id = ""

        while (cursor.moveToNext()) {
            if(int == count) {
                s_id = cursor.getString(1)
                break
            }
            count++
        }
        return s_id
    }

    // 친구 추가 다이얼로그 창에서 확인버튼을 눌렀을 때 check_id_add() 호출
    var dialog_listener_add = object: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                    check_id_add()
            }
        }
    }

    // 친구 삭제 다이얼로그 창에서 확인버튼을 눌렀을 때 check_id_del() 호출
    var dialog_listener_del = object: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                    check_id_del()
            }
        }
    }

    // 메뉴에서 친구 추가/삭제 옵션 클릭했을 때 다이얼로그 창 나타남
    fun dialog(string_1: String, string_2: String) {
        val dlg = AlertDialog.Builder(getActivity())
        dlg.setTitle(string_1) //제목
        val text_id = layoutInflater.inflate(R.layout.dialog, null)
        friend_id = text_id.findViewById<EditText>(R.id.friend_id)
        dlg.setView(text_id)
        dlg.setMessage(string_2) // 메시지

        if(string_1 == "친구 추가") {
            dlg.setPositiveButton("확인",dialog_listener_add) //DB에 아이디 저장 후 -> 액티비티에 출력
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
        else if (string_1 == "친구 삭제") {
            dlg.setPositiveButton("확인",dialog_listener_del) //DB에서 아이디 삭제 후 -> 액티비티에서 삭제
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
    }
}