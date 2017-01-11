package com.polidea.candle.candle

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.BounceInterpolator
import com.google.android.things.pio.PeripheralManagerService
import com.google.android.things.pio.Pwm
import com.polidea.candle.hardware.BoardDefaults
import com.polidea.candle.hardware.LedBrightnessProperty


class CandleFlame(pwm: BoardDefaults.RbpiPwm,
                   offset: Long = 0,
                   frequencyHz: Double = 240.00,
                   dutyCycle: Double = 25.0) {

    private lateinit var led: Pwm

    private val animator: ObjectAnimator by lazy {
        ObjectAnimator
                .ofFloat<Pwm>(led, LedBrightnessProperty(), 100F, 25F)
                .setDuration(ANIMATION_DURATION + offset)
                .apply {
                    interpolator = BounceInterpolator()
                    repeatMode = ValueAnimator.REVERSE
                    repeatCount = ValueAnimator.INFINITE
                }
    }

    init {
        val peripheralMangerService: PeripheralManagerService = PeripheralManagerService()
        led = peripheralMangerService.openPwm(pwm.id)
                .apply {
                    setPwmFrequencyHz(frequencyHz)
                    setPwmDutyCycle(dutyCycle)
                    setEnabled(false)
                }
    }

    fun light() {
        led.setEnabled(true)
        animator.start()
    }

    fun extinguish() {
        led.setEnabled(false)
        animator.cancel()
    }

    companion object {
        private val ANIMATION_DURATION = 350
    }

}