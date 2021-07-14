package decoders

import elements.BDictionary
import elements.BElement
import elements.BList
import elements.BString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder

@ExperimentalSerializationApi
class BDictionaryDecoder(
    bDictionary: BDictionary
) : BAbstractDecoder() {

    override val iterator = bDictionary.value.iterator()
    private var currentValue: BElement = bDictionary

    override fun decodeValue(): Any = currentValue.value

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (iterator.hasNext()) {
            val (key, bElement) = iterator.next()
            currentValue = bElement
            if (descriptor.getElementIndex(key.value).isUnknownName()) {
                continue
            }
            return descriptor.getElementIndex(key.value)
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (currentValue) {
            is BDictionary -> BDictionaryDecoder(currentValue as BDictionary)
            is BList -> BListDecoder(currentValue as BList)
            is BString -> BArrayDecoder(currentValue as BString)
            else -> throw IllegalArgumentException("Illegal BElement")
        }

}

fun Int.isUnknownName() = this == CompositeDecoder.UNKNOWN_NAME