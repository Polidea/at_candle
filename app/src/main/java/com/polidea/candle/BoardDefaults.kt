package com.polidea.candle


import android.os.Build

object BoardDefaults {

    val candleButtonGpio = "BCM21"

    val darknesGpio = "BCM20"

    val boardVariant: String
        get() = Build.DEVICE
}
