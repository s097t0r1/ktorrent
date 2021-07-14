package elements

import Bencoder
import kotlinx.serialization.ExperimentalSerializationApi
import java.util.concurrent.atomic.AtomicInteger

data class BList(override val value: List<BElement>) : BElement(value) {

    override fun toString() = value.toString()

    @ExperimentalSerializationApi
    companion object {
        fun decode(bencode: String, pointer: AtomicInteger): BList {
            pointer.set(pointer.get() + 1)
            val resultList = mutableListOf<BElement>()

            while (bencode[pointer.get()] != Bencoder.END_IDENTIFIER) {
                resultList.add(Bencoder.decode(bencode, pointer))
            }
            pointer.set(pointer.get() + 1)

            return BList(resultList)
        }
    }
}