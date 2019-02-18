package comunication

abstract class Communication(internal val id: Int) {


    abstract fun sendByte(data: Byte, registry: Int = -1)

    abstract fun sendBytes(data: ByteArray, registry: Int = -1)

    /**
     * Requst data form a slave.
     * @param registry the registry to get the data (not requierd)
     */
    abstract fun readByte(registry: Int = -1) : Int
}