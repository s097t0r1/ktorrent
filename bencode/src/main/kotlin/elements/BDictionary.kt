package elements

import Bencoder
import DICTIONARY_IDENTIFIER
import END_IDENTIFIER
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.atomic.AtomicInteger

class BDictionary(override val value: Map<BString, BElement>) : BElement(value), Map<BString, BElement> by value {

    @ExperimentalSerializationApi
    companion object {

        fun decode(bencode: String, pointer: AtomicInteger): BDictionary {
            val resultMap = mutableMapOf<BString, BElement>()

            pointer.set(pointer.get() + 1)

            while (bencode[pointer.get()] != END_IDENTIFIER) {
                resultMap.put(
                    BString.decode(bencode, pointer),
                    Bencoder.decode(bencode, pointer)
                )
            }

            pointer.set(pointer.get() + 1)
            return BDictionary(resultMap)
        }

        fun encode(bDictionary: BDictionary) = buildString {
            append(DICTIONARY_IDENTIFIER)
            for ((key, value) in bDictionary) {
                append(Bencoder.encode(key))
                append(Bencoder.encode(value))
            }
            append(END_IDENTIFIER)
        }

    }

}
