package com.example.main.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.DeleteSettings
import com.example.main.NextCalActivity
import com.example.main.R
import com.example.main.UpdateSettings

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    lateinit var btnUpdate : Button
    lateinit var btnDelete : Button
    lateinit var btnVersion : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnUpdate = view.findViewById(R.id.btnUpdate)
        btnDelete = view.findViewById(R.id.btnDelete)
        btnVersion = view.findViewById(R.id.btnVersion)

        btnUpdate.setOnClickListener {
            val intent = Intent(getActivity(), UpdateSettings::class.java)
            startActivity(intent)
        }
        btnDelete.setOnClickListener {
            var builder = AlertDialog.Builder(getActivity())
            builder.setTitle("회원 탈퇴")
            builder.setMessage("정말로 탈퇴하시겠습니까?")
            var listener = object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val intent = Intent(getActivity(), DeleteSettings::class.java)
                    startActivity(intent)
                }
            }
            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)
            builder.show()
        }
        btnVersion.setOnClickListener {
            var builder = AlertDialog.Builder(getActivity())
            builder.setTitle("버전 정보")
            builder.setMessage("최신 버전을 사용 중입니다")
            builder.setPositiveButton("확인", null)
            builder.show()
        }
    }
}