package elements

import Bencoder
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.atomic.AtomicInteger

class BDictionary(override val value: Map<BString, BElement>) : BElement(value) {

    override fun toString(): String = value.toString()

    @ExperimentalSerializationApi
    companion object {
        fun decode(bencode: String, pointer: AtomicInteger): BDictionary {
            val resultMap = mutableMapOf<BString, BElement>()

            pointer.set(pointer.get() + 1)

            while (bencode[pointer.get()] != Bencoder.END_IDENTIFIER) {
                resultMap.put(
                    BString.decode(bencode, pointer),
                    Bencoder.decode(bencode, pointer)
                )
            }

            pointer.set(pointer.get() + 1)
            return BDictionary(resultMap)
        }
    }
}