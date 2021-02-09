package com.example.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.main.Answeractivity
import com.example.main.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.util.*
import android.widget.Toast.makeText as toastMakeText

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var writeButton : FloatingActionButton
    lateinit var refreshButton : FloatingActionButton
    lateinit var fixQButton : FloatingActionButton
    lateinit var shareButton : FloatingActionButton
    lateinit var question : EditText
    lateinit var date : TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        writeButton = view.findViewById(R.id.writeButton)
        refreshButton = view.findViewById(R.id.refreshButton)
        fixQButton = view.findViewById(R.id.fixQButton)
        shareButton = view.findViewById(R.id.shareButton)
        question = view.findViewById(R.id.questionText)
        date = view.findViewById(R.id.date)
        question.isEnabled = false

        // 상단바 이름 변경
        (activity as AppCompatActivity).supportActionBar?.title = "홈"


        var intent = Intent(getActivity(), Answeractivity::class.java)
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR).toString()
        val month = (cal.get(Calendar.MONTH)+1).toString()
        val day = cal.get(Calendar.DATE).toString()
        date.setText("$year" +"/" + "$month" + "/"+ "$day")

        writeButton.setOnClickListener{
            intent.putExtra("question", "Q. " + question.getText().toString())
           // intent.putExtra("cal", cal)
            intent.putExtra("year", year)
            intent.putExtra("month", month)
            intent.putExtra("day", day)
            startActivity(intent)
            var wB = Toast.makeText(view.context, "답변 입력",Toast.LENGTH_SHORT).show()
        }
        fixQButton.setOnClickListener{
            question.isEnabled = true
            var fB = Toast.makeText(view.context, "질문 수정",Toast.LENGTH_SHORT).show()


        }
        shareButton.setOnClickListener{
            var sB = Toast.makeText(view.context, "공유하기",Toast.LENGTH_SHORT).show()
            var share_intent = Intent(Intent.ACTION_SEND)
            share_intent.type = "text/plain"
            share_intent.putExtra(Intent.EXTRA_TEXT, question.getText().toString())
            val chooser = Intent.createChooser(share_intent, "공유하기")
            startActivity(chooser)
        }

    }

}
