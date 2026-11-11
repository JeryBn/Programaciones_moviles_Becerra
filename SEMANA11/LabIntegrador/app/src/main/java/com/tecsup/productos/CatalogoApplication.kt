package com.tecsup.productos

import android.app.Application

class CatalogoApplication : Application() {
    val container by lazy { AppContainer(this) }
}
