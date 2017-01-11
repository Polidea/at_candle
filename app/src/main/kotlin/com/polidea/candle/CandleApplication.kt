package com.polidea.candle


import android.app.Application
import com.polidea.candle.hardware.BoardDefaults


class CandleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        checkBoard()
    }

    private fun checkBoard() {
        if (!BoardDefaults.isRPI3) {
            throw IllegalStateException("Only RaspberryPi3 is supported")
        }
    }
}
