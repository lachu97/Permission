package com.example.permission

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class viewmodel : ViewModel() {
    private var _state = mutableStateOf(false)
    val mystate get() = _state.value
    fun setstate(result:Boolean){
        _state.value =result
    }
}