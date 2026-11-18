package com.tecsup.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacion: UbicacionRepository,
    private val lugares: LugaresRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        requireNotNull(modelClass.cast(MapaViewModel(ubicacion, lugares)))
}
