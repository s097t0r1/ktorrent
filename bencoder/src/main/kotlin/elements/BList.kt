package elements

import Bencoder
import java.util.concurrent.atomic.AtomicInteger

data class BList(val value: List<BElement>) : BElement() {

    override fun toString() = value.toString()

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