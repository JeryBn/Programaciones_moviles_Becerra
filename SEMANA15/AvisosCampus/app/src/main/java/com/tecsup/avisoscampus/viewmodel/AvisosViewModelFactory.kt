package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.avisoscampus.data.AvisosRepository

class AvisosViewModelFactory(private val repo: AvisosRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        requireNotNull(modelClass.cast(AvisosViewModel(repo)))
}
