package com.example.main.ui.friends

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.main.R


class FriendsFragment : Fragment() {

    private lateinit var friendsViewModel: FriendsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        friendsViewModel =
                ViewModelProvider(this).get(FriendsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_friends, container, false)
        val textView: TextView = root.findViewById(R.id.text_friends)
        friendsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        setHasOptionsMenu(true)
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

    fun dialog(string_1: String, string_2: String) {
        val dlg = AlertDialog.Builder(getActivity())
        dlg.setTitle(string_1) //제목
        val text_id = layoutInflater.inflate(R.layout.dialog, null)
        dlg.setView(text_id)
        dlg.setMessage(string_2) // 메시지

        if(string_1 == "친구 추가") {
            dlg.setPositiveButton("확인",null) //DB에 아이디 저장 후 -> 액티비티에 출력
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
        else if (string_1 == "친구 삭제") {
            dlg.setPositiveButton("확인",null) //DB에서 아이디 삭제 후 -> 액티비티에서 삭제
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
    }
}