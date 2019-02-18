package comunication

interface Com {

    fun write(data: Int)

    fun write(data:String)

    fun readInt(registry: Int) : Int

    fun readInts(registry: Int) : IntArray

    fun readDouble(registry: Int) : Double

    fun readDoubels(registry: Int) : DoubleArray

    fun readString(registry: Int) : String
}