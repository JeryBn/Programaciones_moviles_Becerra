package com.example.lab05

import org.junit.Assert.assertEquals
import org.junit.Test

class FavoritosTest {
    @Test fun `agrega y quita el mismo favorito`() {
        val agregado = toggleFavorito(emptySet(), 3)
        assertEquals(setOf(3), agregado)
        assertEquals(emptySet<Int>(), toggleFavorito(agregado, 3))
    }
}
