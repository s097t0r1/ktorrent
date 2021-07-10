import elements.*
import java.io.File
import java.text.ParseException
import java.util.concurrent.atomic.AtomicInteger

object Bencoder {
    fun decode(pathToFile: String): BElement {
        val pointer = AtomicInteger()
        val bencode = File(pathToFile)
            .inputStream()
            .readBytes()
            .toString(Charsets.US_ASCII)


        return decode(bencode, pointer)
    }

    fun decode(bencode: String, pointer: AtomicInteger): BElement {
        return when (bencode[pointer.get()]) {
            in '1'..'9' -> BString.decode(bencode, pointer)
            INTEGER_IDENTIFIER -> BInteger.decode(bencode, pointer)
            LIST_IDENTIFIER -> BList.decode(bencode, pointer)
            DICTIONARY_IDENTIFIER -> BDictionary.decode(bencode, pointer)
            else -> throw ParseException("Illegal symbol", pointer.get())
        }
    }

    const val INTEGER_IDENTIFIER = 'i'
    const val LIST_IDENTIFIER = 'l'
    const val DICTIONARY_IDENTIFIER = 'd'
    const val END_IDENTIFIER = 'e'
}