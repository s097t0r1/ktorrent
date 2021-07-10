package elements

import Bencoder
import java.util.concurrent.atomic.AtomicInteger

class BDictionary(val value: Map<BString, BElement>) : BElement() {

    override fun toString(): String = value.toString()

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