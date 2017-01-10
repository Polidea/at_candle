package com.polidea.candle


import android.app.Application

class CandleApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if ("rpi3" != BoardDefaults.boardVariant) {
            throw IllegalStateException("Only RaspberryPi3 is supported")
        }
    }
}
