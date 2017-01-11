package com.polidea.candle


import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.things.contrib.driver.button.Button
import com.polidea.candle.candle.Candle
import com.polidea.candle.hardware.BoardDefaults
import com.polidea.candle.hardware.ButtonInputDriver
import java.io.IOException


class MainActivity : Activity() {

    private val candleButton: ButtonInputDriver by lazy {
        ButtonInputDriver(
                BoardDefaults.candleButtonGpio,
                Button.LogicState.PRESSED_WHEN_LOW)
    }

    private val darknessDetector: ButtonInputDriver by lazy {
        ButtonInputDriver(
                BoardDefaults.darknesGpio,
                Button.LogicState.PRESSED_WHEN_LOW)
    }

    private val candle: Candle by lazy { Candle() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        candleButton.listen(candleButtonListener)
        darknessDetector.listen(darknessDetectorListener)

        candle.extinguish()
    }

    private val darknessDetectorListener = { isDark: Boolean ->
        if (isDark) {
            candle.light()
        } else {
            candle.extinguish()
        }
    }

    private val candleButtonListener = {pressed: Boolean ->
        if (pressed) {
            candle.switch()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        candleButton.unregister()
        try {
            candleButton.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing Button driver", e)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
