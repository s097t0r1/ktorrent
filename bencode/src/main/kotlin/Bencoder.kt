import decoders.BDictionaryDecoder
import elements.*
import encoders.BDictionaryEncoder
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import java.io.ByteArrayOutputStream
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

    fun <T> encodeTo(serializer: SerializationStrategy<T>, value: T): ByteArray {
        val byteOutputStream = ByteArrayOutputStream()
        val encoder = BDictionaryEncoder(byteOutputStream)
        encoder.encodeSerializableValue(serializer, value)
        return byteOutputStream.toByteArray()
    }

    inline fun <reified T> encodeTo(value: T) = encodeTo(serializer(), value)

    private fun decode(pathToFile: String): BElement {
        val pointer = AtomicInteger()
        val bencode = File(pathToFile)
            .inputStream()
            .readBytes()
            .toString(Charsets.ISO_8859_1)

        return decode(bencode, pointer)
    }

    inline fun <reified T> decode(bencode: String, pointer: AtomicInteger): T {
        return when (bencode[pointer.get()]) {
            in '1'..'9' -> BString.decode(bencode, pointer)
            INTEGER_IDENTIFIER -> BInteger.decode(bencode, pointer)
            LIST_IDENTIFIER -> BList.decode(bencode, pointer)
            DICTIONARY_IDENTIFIER -> BDictionary.decode(bencode, pointer)
            else -> throw ParseException("Illegal symbol", pointer.get())
        } as T
    }

    fun encode(bElement: BElement): String = when (bElement) {
        is BDictionary -> BDictionary.encode(bElement)
        is BList -> BList.encode(bElement)
        is BString -> BString.encode(bElement)
        is BInteger -> BInteger.encode(bElement)
        else -> throw IllegalArgumentException("Unknown BElement")
    }

}

const val INTEGER_IDENTIFIER = 'i'
const val LIST_IDENTIFIER = 'l'
const val DICTIONARY_IDENTIFIER = 'd'
const val END_IDENTIFIER = 'e'
