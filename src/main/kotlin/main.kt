import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import metainfo.TorrentMetaInfo
import java.security.MessageDigest

val client = HttpClient(CIO) {
    install(Logging)
}

@ExperimentalSerializationApi
fun main(args: Array<String>) {
    val torrentMetaInfo = Bencoder.decodeFrom<TorrentMetaInfo>(args[0])
    val byteArray = Bencoder.encodeTo(torrentMetaInfo.info)
    val peerId = generatePeerId(20)
    runBlocking {
        val response: HttpResponse = client.get(torrentMetaInfo.announce) {
            url.parameters.urlEncodingOption = UrlEncodingOption.NO_ENCODING
            parameter("info_hash", byteArray)
            parameter("peer_id", peerId)
            parameter("port", 52385)
            parameter("uploaded", 0)
            parameter("downloaded", 0)
            parameter("left", torrentMetaInfo.info.length)
            parameter("compact", 1)
            parameter("event", "started")
        }
    }
}

fun generatePeerId(length: Int): String =
    (0 until length)
        .map { ('A'..'Z').random() }
        .joinToString("")

