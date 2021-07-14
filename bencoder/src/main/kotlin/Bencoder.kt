import decoders.BDictionaryDecoder
import elements.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.serializer
import java.io.File
import java.text.ParseException
import java.util.concurrent.atomic.AtomicInteger

@ExperimentalSerializationApi
object Bencoder {

    inline fun <reified T> decodeFrom(pathToFile: String): T = decodeFrom(pathToFile, serializer())

    fun <T> decodeFrom(pathToFile: String, deserializer: DeserializationStrategy<T>): T {
        val decoder = BDictionaryDecoder(decode(pathToFile) as BDictionary)
        return decoder.decodeSerializableValue(deserializer)
    }

    private fun decode(pathToFile: String): BElement {
        val pointer = AtomicInteger()
        val bencode = File(pathToFile)
            .inputStream()
            .readBytes()
            .toString(Charsets.US_ASCII)

        return decode(bencode, pointer)
    }

    internal fun decode(bencode: String, pointer: AtomicInteger): BElement {
        return when (bencode[pointer.get()]) {
            in '1'..'9' -> BString.decode(bencode, pointer)
            INTEGER_IDENTIFIER -> BInteger.decode(bencode, pointer)
            LIST_IDENTIFIER -> BList.decode(bencode, pointer)
            DICTIONARY_IDENTIFIER -> BDictionary.decode(bencode, pointer)
            else -> throw ParseException("Illegal symbol", pointer.get())
        }
    }

    private const val INTEGER_IDENTIFIER = 'i'
    private const val LIST_IDENTIFIER = 'l'
    private const val DICTIONARY_IDENTIFIER = 'd'

    internal const val END_IDENTIFIER = 'e'
}