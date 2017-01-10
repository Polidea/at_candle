package com.polidea.candle

import android.view.InputDevice
import com.google.android.things.contrib.driver.button.Button
import com.google.android.things.userdriver.InputDriver
import com.google.android.things.userdriver.UserDriverManager
import java.io.IOException

class ButtonInputDriver
/**
 * Create a new framework input driver for a button connected on given GPIO pin.
 * The driver emits [android.view.KeyEvent] with the given keycode when registered.
 * @param pin GPIO pin where the button is connected.
 * *
 * @param logicLevel Logic level when the button is considered pressed.
 * *
 * @param keycode keycode to be emitted.
 * *
 * @throws IOException
 * *
 * @see .listen

 * @return new input driver instance.
 */
@Throws(IOException::class)
constructor(pin: String, logicLevel: Button.LogicState) : AutoCloseable {

    private var mDevice: Button? = null
    private var mDriver: InputDriver? = null

    init {
        mDevice = Button(pin, logicLevel)
        if (mDevice == null) {
            throw IllegalStateException("cannot registered closed driver")
        }
        mDriver = build()
        UserDriverManager.getManager().registerInputDriver(mDriver)
    }


    /**
     * Close the driver and the underlying device.
     * @throws IOException
     */
    @Throws(IOException::class)
    override fun close() {
        unregister()
        if (mDevice != null) {
            try {
                mDevice!!.close()
            } finally {
                mDevice = null
            }
        }
    }

    /**
     * Register the driver in the framework.
     */
    fun listen(buttonListener: (Boolean) -> Unit) {
            mDevice!!.setOnButtonEventListener { sdf, pressed ->
                buttonListener(pressed)
            }

    }

    /**
     * Unregister the driver from the framework.
     */
    fun unregister() {
        if (mDriver != null) {
            UserDriverManager.getManager().registerInputDriver(mDriver)
            mDriver = null
        }
    }

    companion object {
        private val DRIVER_NAME = "Button"
        private val DRIVER_VERSION = 1

        internal fun build(): InputDriver {
            val inputDriver = InputDriver.builder(InputDevice.SOURCE_CLASS_BUTTON)
                    .setName(DRIVER_NAME)
                    .setVersion(DRIVER_VERSION)
                    .setKeys(intArrayOf(34))
                    .build()
            return inputDriver
        }
    }
}