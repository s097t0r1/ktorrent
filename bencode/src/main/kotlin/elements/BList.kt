package elements

import Bencoder
import END_IDENTIFIER
import LIST_IDENTIFIER
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.atomic.AtomicInteger

data class BList(override val value: List<BElement>) : BElement(value), List<BElement> by value {

    @ExperimentalSerializationApi
    companion object {

        fun decode(bencode: String, pointer: AtomicInteger): BList {
            pointer.set(pointer.get() + 1)
            val resultList = mutableListOf<BElement>()

            while (bencode[pointer.get()] != END_IDENTIFIER) {
                resultList.add(Bencoder.decode(bencode, pointer))
            }
            pointer.set(pointer.get() + 1)

            return BList(resultList)
        }

        fun encode(bList: BList) = buildString {
            append(LIST_IDENTIFIER)
            for (bElement in bList) {
                append(Bencoder.encode(bElement))
            }
            append(END_IDENTIFIER)
        }

    }
}
