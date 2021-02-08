package com.example.main.ui.friends

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.main.DBManager
import com.example.main.MainActivity
import com.example.main.R


class FriendsFragment : Fragment() {

    private lateinit var friendsViewModel: FriendsViewModel

    lateinit var friend_id: EditText

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
        val textView: TextView = root.findViewById(R.id.text_friends)
        friendsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        setHasOptionsMenu(true)
        friend_id =sub.findViewById(R.id.friend_id)
        dbManager = DBManager(activity, "registerDB", null, 1)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.friend_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.group_add -> {
                dialog("친구 추가", "그룹에 추가할 친구의 아이디를 입력해주세요.")
                return true
            }
            R.id.group_del -> {
                dialog("친구 삭제", "그룹에 삭제할 친구의 아이디를 입력해주세요.")
                return true
            }
            R.id.friend_add -> {
                dialog("친구 추가", "추가할 친구의 아이디를 입력해주세요.")
                return true
            }
            R.id.friend_del -> {
                dialog("친구 삭제", "삭제할 친구의 아이디를 입력해주세요.")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var dialog_listener_add = object: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            sqlitedb = dbManager.readableDatabase

            var cursor: Cursor
            cursor = sqlitedb.rawQuery("SELECT * FROM register;", null)

            var idData: String = ""
            var nameData: String = ""

            while (cursor.moveToNext()){
                idData = cursor.getString(1)
                nameData = cursor.getString(0)

                id = friend_id.text.toString()

                if(id == idData) {
                    Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                else if(id != idData) {
                    Toast.makeText(activity, "회원정보가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            cursor.close()
            sqlitedb.close()
        }
    }

    var dialog_listener_del = object: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                    Toast.makeText(activity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun dialog(string_1: String, string_2: String) {
        val dlg = AlertDialog.Builder(getActivity())
        dlg.setTitle(string_1) //제목
        val text_id = layoutInflater.inflate(R.layout.dialog, null)
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