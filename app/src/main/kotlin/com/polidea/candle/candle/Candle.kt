package com.polidea.candle.candle

import com.polidea.candle.hardware.BoardDefaults


class Candle {

    companion object {
        private val SECOND_FLAME_OFFSET = 66L
    }

    private var firstFlame: CandleFlame = CandleFlame(BoardDefaults.RbpiPwm.PWM0)
    private var secondFlame: CandleFlame = CandleFlame(BoardDefaults.RbpiPwm.PWM1, SECOND_FLAME_OFFSET)

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

