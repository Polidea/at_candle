package com.polidea.candle.hardware

import android.view.InputDevice
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.userdriver.InputDriver
import com.google.android.things.userdriver.UserDriverManager

class ButtonInputDriver(pin: String,
                        logicLevel: Button.LogicState) : AutoCloseable {

    private var hardwareButton: Button?
    private var inputDriver: InputDriver?

    init {
        hardwareButton = Button(pin, logicLevel)
        if (hardwareButton == null) {
            throw IllegalStateException("cannot registered closed driver")
        }
        inputDriver = buildButtonInputDriver()
        UserDriverManager.getManager().registerInputDriver(inputDriver)
    }

    override fun close() {
        unregister()
        try {
            hardwareButton?.close()
        } finally {
            hardwareButton = null
        }
    }

    fun listen(buttonListener: (Boolean) -> Unit) {
        hardwareButton!!.setOnButtonEventListener { _, pressed ->
            buttonListener(pressed)
        }
    }

    fun unregister() {
        inputDriver?.unregister()
        inputDriver = null
    }

    private fun InputDriver.unregister() {
        UserDriverManager.getManager().unregisterInputDriver(this)
    }

    private fun buildButtonInputDriver(): InputDriver
        = InputDriver.builder(InputDevice.SOURCE_CLASS_BUTTON)
            .setName(DRIVER_NAME)
            .setVersion(DRIVER_VERSION)
            .setKeys(intArrayOf(34))
            .build()


    companion object {
        private val DRIVER_NAME = "Button"
        private val DRIVER_VERSION = 1
    }
}