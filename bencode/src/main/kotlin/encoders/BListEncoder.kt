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
class BListEncoder(outputStream: OutputStream) : BPrimitiveEncoder(outputStream) {

    override fun encodeValue(value: Any) {
        when (value) {
            is ByteArray -> encodeByteArray(value)
            else -> super.encodeValue(value)
        }
    }

    private fun encodeByteArray(value: ByteArray) {
        encodeString(value.toString(Charsets.ISO_8859_1))
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