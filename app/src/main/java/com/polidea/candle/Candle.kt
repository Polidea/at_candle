package com.polidea.candle

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.FloatProperty
import android.util.Log
import android.view.animation.BounceInterpolator
import com.google.android.things.pio.PeripheralManagerService
import com.google.android.things.pio.Pwm
import java.io.IOException


class Candle {

    private var firstFlame: CandleFlame = CandleFlame(RbpiPwm.PWM0)
    private var secondFlame: CandleFlame = CandleFlame(RbpiPwm.PWM1, 66)

    private val flames = listOf(firstFlame, secondFlame)

    private var isTurnedOn = false

    fun light() {
        if (!isTurnedOn) {
            isTurnedOn = true
            flames.forEach { it.light() }
        }
    }

    fun extinguish() {
        if (isTurnedOn) {
            flames.forEach { it.extinguish() }
            isTurnedOn = false
        }
    }

    fun switch() {
        if (isTurnedOn) {
            extinguish()
        } else {
            light()
        }
    }
}

class CandleFlame(pwm: RbpiPwm,
                  offset: Long = 0,
                  frequencyHz: Double = 240.00,
                  dutyCycle: Double = 25.0) {

    private lateinit var led: Pwm

    private val animator: ObjectAnimator by lazy {
            ObjectAnimator
                .ofFloat<Pwm>(led, LedBrightnessProperty(), 100F, 25F)
                .setDuration(350 + offset)
                .apply {
                    interpolator = BounceInterpolator()
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                }
        }

    init {
        val peripheralMangerService: PeripheralManagerService = PeripheralManagerService()
        led = peripheralMangerService.openPwm(pwm.id)
        led.setPwmFrequencyHz(frequencyHz)
        led.setPwmDutyCycle(dutyCycle)
        led.setEnabled(false)
    }

    fun light() {
        led.setEnabled(true)
        animator.start()
    }

    fun extinguish() {
        led.setEnabled(false)
        animator.cancel()
    }

}

enum  class RbpiPwm(val id: String) {
    PWM0("PWM0"),
    PWM1("PWM1");
}

private class LedBrightnessProperty internal constructor() : FloatProperty<Pwm>("PWM Brightness") {

    private val TAG = "BrightnessProperty"

    private var mValue: Float = 0.toFloat()

    override fun setValue(pwm: Pwm, value: Float) {
        mValue = Math.max(0f, Math.min(value, 100f))
        try {
            pwm.setPwmDutyCycle(mValue.toDouble())
        } catch (e: IOException) {
            Log.w(TAG, "Unable to set PWM duty cycle", e)
        }

    }

    override fun get(pwm: Pwm): Float? {
        return mValue
    }
}

