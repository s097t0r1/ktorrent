package decoders

import elements.BDictionary
import elements.BElement
import elements.BList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder

@ExperimentalSerializationApi
class BListDecoder(bList: BList) : BAbstractDecoder() {

    override val iterator = bList.value.iterator().withIndex()
    private var currentValue: BElement = bList

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (iterator.hasNext()) {
            val (key, bElement) = iterator.next()
            currentValue = bElement
            return key
        }

        return CompositeDecoder.DECODE_DONE
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (currentValue) {
            is BDictionary -> BDictionaryDecoder(currentValue as BDictionary)
            is BList -> BListDecoder(currentValue as BList)
            else -> throw IllegalStateException("Illegal BElement")
        }
}