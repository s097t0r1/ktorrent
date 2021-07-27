package encoders

import DICTIONARY_IDENTIFIER
import END_IDENTIFIER
import LIST_IDENTIFIER
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import java.io.OutputStream

@ExperimentalSerializationApi
open class BDictionaryEncoder(outputStream: OutputStream) : BPrimitiveEncoder(outputStream) {

    override fun encodeValue(value: Any) {
        when (value) {
            is ByteArray -> encodeByteArray(value)
            else -> super.encodeValue(value)
        }
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        encodeString(descriptor.getElementName(index))
        return true
    }

    private fun encodeByteArray(value: ByteArray) {
        outputStream.write(value)
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return when (descriptor.kind) {
            StructureKind.CLASS -> {
                outputStream.write(DICTIONARY_IDENTIFIER.code)
                BDictionaryEncoder(outputStream)
            }
            StructureKind.LIST -> {
                outputStream.write(LIST_IDENTIFIER.code)
                BListEncoder(outputStream)
            }
            else -> throw SerializationException("Unknown element")
        }
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        outputStream.write(END_IDENTIFIER.code)
    }
}