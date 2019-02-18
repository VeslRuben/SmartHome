package comunication

import com.pi4j.io.i2c.I2CBus
import com.pi4j.io.i2c.I2CDevice
import com.pi4j.io.i2c.I2CFactory
import comunication.I2cCom.Companion.i2c


class I2cCom(slaveId: Int) : Communication(slaveId) {

    private val device: I2CDevice

    companion object {
        val i2c = I2CFactory.getInstance(I2CBus.BUS_1)!!
    }

    init {
        this.device = i2c.getDevice(this.id)
    }

    /**
     * sends one byte of data to a unit. sends it to a registry if spesifyd.
     * @param data the byte to send.
     * @param registry the registry to send to.
     */
    override fun sendByte(data: Byte, registry: Int) {
        if (registry == -1) {
            this.device.write(data)
        } else{
            this.device.write(registry, data)
        }
    }

    /**
     * sends a sett of byte to a unit. sends it to a registry if spesifyd.
     * @param data byteArray of data to send.
     * @param registry the registry to send to.
     */
    override fun sendBytes(data: ByteArray, registry: Int) {
        if (registry == -1) {
            this.device.write(data)
        } else {
            this.device.write(registry, data)
        }

    }

    /**
     * Reads out one byte of data form the unit. reads form a registry if spesifyd.
     * @param registry registry to read from.
     * @return The data sent back form the unit.
     */
    override fun readByte(registry: Int): Int {
        return if (registry == -1) {
            device.read(registry)
        } else {
            device.read()
        }
        //TODO("change so its sends first then reads. registry parameter not geting reacivd on arduino")
    }
}