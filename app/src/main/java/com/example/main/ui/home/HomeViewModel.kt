package com.example.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
      // 홈 프레그먼트의 뷰 모델
    }
    val text: LiveData<String> = _text
}