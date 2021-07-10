package elements

import Bencoder
import java.text.ParseException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Integers are encoded as follows: i<integer encoded in base ten ASCII>e
 * The initial i and trailing e are beginning and ending delimiters.
 * Example: i3e represents the integer "3"
 */
data class BInteger(val value: Int) : BElement() {

    override fun toString(): String = value.toString()

    companion object {
        fun decode(bencode: String, pointer: AtomicInteger): BInteger {
            pointer.set(pointer.get() + 1)
            val indexOfEnd = bencode.indexOf(Bencoder.END_IDENTIFIER, pointer.get())
            if (indexOfEnd < pointer.get()) {
                throw ParseException("End identifier not found", pointer.get())
            }
            val value = bencode.substring(pointer.get(), indexOfEnd).toInt()
            pointer.set(indexOfEnd + 1)
            return BInteger(value)
        }
    }
}