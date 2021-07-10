package elements

import java.text.ParseException
import java.util.concurrent.atomic.AtomicInteger

/**
 * Byte strings are encoded as follows: <string length encoded in base ten ASCII>:<string data>
 * Example: 8:announce
 */
data class BString(val value: String) : BElement() {

    override fun toString(): String = value

    companion object {
        fun decode(bencode: String, pointer: AtomicInteger): BString {
            val lengthOfString = length(bencode, pointer)
            val startIndex = pointer.get()

            pointer.set(startIndex + lengthOfString)

            val value = bencode.substring(startIndex, startIndex + lengthOfString)
            return BString(value)
        }

        private fun length(bencode: String, pointer: AtomicInteger): Int {
            val startIndexOfLength = pointer.get()
            val endIndexOfLength = bencode.indexOf(':', startIndexOfLength)
            if (endIndexOfLength < pointer.get()) {
                throw ParseException("Illegal length of string", pointer.get())
            }
            pointer.set(endIndexOfLength + 1)
            return bencode.substring(startIndexOfLength, endIndexOfLength).toInt()
        }

    }
}