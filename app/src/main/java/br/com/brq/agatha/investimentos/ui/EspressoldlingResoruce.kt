package br.com.brq.agatha.investimentos.ui

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoldlingResoruce{
    private const val RESOURCE = "GLOBAL"

    @JvmField val counting = CountingIdlingResource(RESOURCE)

    fun increment(){
        counting.increment()
    }

    fun decrement(){
        if(!counting.isIdleNow){
            counting.decrement()
        }
    }
}