package com.polidea.candle.hardware


import android.os.Build

const val RPI3_BARD = "rpi3"

object BoardDefaults {

    val candleButtonGpio = "BCM21"
    val darknesGpio = "BCM20"

    enum class RbpiPwm(val id: String) {
        PWM0("PWM0"),
        PWM1("PWM1");
    }

    val boardVariant: String
        get() = Build.DEVICE

    val isRPI3: Boolean by lazy { RPI3_BARD == boardVariant }
}
