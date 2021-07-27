package encoders

import elements.BInteger
import elements.BString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import java.io.OutputStream

@ExperimentalSerializationApi
open class BPrimitiveEncoder(protected val outputStream: OutputStream) : AbstractEncoder() {

    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun encodeInt(value: Int) {
        outputStream.write(BInteger.encode(BInteger(value)).toByteArray())
    }

    override fun encodeString(value: String) {
        outputStream.write(BString.encode(BString(value)).toByteArray())
    }

    override fun encodeByte(value: Byte) {
        outputStream.write(value.toInt())
    }



    override fun encodeNull() { }


}