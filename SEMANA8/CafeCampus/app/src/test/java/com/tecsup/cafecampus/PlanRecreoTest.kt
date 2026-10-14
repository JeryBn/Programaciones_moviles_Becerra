package com.tecsup.cafecampus
import org.junit.Test
import org.junit.Assert.*
import com.tecsup.cafecampus.model.calcularRecreo
class PlanRecreoTest {
 @Test fun colaVaciaCabe() {val p=calcularRecreo(0,0,5,true);assertEquals(4,p.minutosEstimados);assertTrue(p.cabe)}
 @Test fun colaLargaNoCabe() {val p=calcularRecreo(4,2,10,true);assertEquals(14,p.minutosEstimados);assertFalse(p.cabe)}
 @Test fun cerradaNoPrometeTiempo() {assertFalse(calcularRecreo(0,0,20,false).cabe)}
 @Test fun bordeExactoCabe() {assertTrue(calcularRecreo(3,0,10,true).cabe)}
 @Test(expected=IllegalArgumentException::class) fun rechazaValoresInvalidos() {calcularRecreo(-1,0,5,true)}
}
