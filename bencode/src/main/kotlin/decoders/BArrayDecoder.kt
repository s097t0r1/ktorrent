package decoders

import elements.BString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import java.lang.IllegalStateException

@ExperimentalSerializationApi
class BArrayDecoder(bString: BString) : BAbstractDecoder() {

    override val iterator = bString.value.iterator().withIndex()
    private var currentValue: Byte = bString.value[0].code.toByte()

    override fun decodeValue(): Any = currentValue

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (iterator.hasNext()) {
            val (key, value) = iterator.next()
            currentValue = value.code.toByte()
            return key
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        throw IllegalArgumentException("Illegal element in array of bytes")
}