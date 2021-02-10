package com.example.main.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.main.NextCalActivity
import com.example.main.R
import com.example.main.UpdateSettings

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    lateinit var btnChangeName : Button
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

        btnChangeName = view.findViewById(R.id.btnUpdateInfo)
        btnDelete = view.findViewById(R.id.btnDelete)
        btnVersion = view.findViewById(R.id.btnVersion)

        btnChangeName.setOnClickListener {
            val intent = Intent(getActivity(), UpdateSettings::class.java)
            startActivity(intent)
        }
    }
}