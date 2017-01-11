package com.polidea.candle.hardware

import android.util.FloatProperty
import android.util.Log
import com.google.android.things.pio.Pwm
import java.io.IOException


class LedBrightnessProperty internal constructor() : FloatProperty<Pwm>("PWM Brightness") {

    private var value = 0f

    override fun setValue(pwm: Pwm, newValue: Float) {
        value = Math.max(0f, Math.min(newValue, 100f))
        try {
            pwm.setPwmDutyCycle(value.toDouble())
        } catch (e: IOException) {
            Log.w(TAG, "Unable to set PWM duty cycle", e)
        }
    }

    override fun get(pwm: Pwm): Float? = value

    companion object {
        private val TAG = LedBrightnessProperty::class.java.name
    }

}