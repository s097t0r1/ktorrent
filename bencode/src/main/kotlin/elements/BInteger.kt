package elements

import Bencoder
import END_IDENTIFIER
import INTEGER_IDENTIFIER
import kotlinx.serialization.ExperimentalSerializationApi
import java.text.ParseException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Integers are encoded as follows: i<integer encoded in base ten ASCII>e
 * The initial i and trailing e are beginning and ending delimiters.
 * Example: i3e represents the integer "3"
 */
data class BInteger(override val value: Int) : BElement(value) {

    override fun toString(): String = value.toString()

    @ExperimentalSerializationApi
    companion object {

        fun decode(bencode: String, pointer: AtomicInteger): BInteger {
            pointer.set(pointer.get() + 1)
            val indexOfEnd = bencode.indexOf(END_IDENTIFIER, pointer.get())
            if (indexOfEnd < pointer.get()) {
                throw ParseException("End identifier not found", pointer.get())
            }
            val value = bencode.substring(pointer.get(), indexOfEnd).toInt()
            pointer.set(indexOfEnd + 1)
            return BInteger(value)
        }

        fun encode(bInteger: BInteger) = buildString {
            append(INTEGER_IDENTIFIER)
            append(bInteger.value)
            append(END_IDENTIFIER)
        }
    }

}
