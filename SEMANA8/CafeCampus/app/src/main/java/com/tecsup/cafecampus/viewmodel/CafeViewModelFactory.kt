package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
class CafeViewModelFactory<T:ViewModel>(private val crear:()->T):ViewModelProvider.Factory {
 @Suppress("UNCHECKED_CAST") override fun <V:ViewModel> create(modelClass:Class<V>):V=crear() as V
}

