package decoders

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule


@ExperimentalSerializationApi
abstract class BAbstractDecoder : AbstractDecoder() {

    abstract val iterator: Iterator<*>

    override val serializersModule: SerializersModule = EmptySerializersModule

}