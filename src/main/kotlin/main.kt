import kotlinx.serialization.ExperimentalSerializationApi
import metainfo.TorrentMetaInfo

@ExperimentalSerializationApi
fun main(args: Array<String>) {
    val torrentMetaInfo = Bencoder.decodeFrom<TorrentMetaInfo>(args[0])
    print(torrentMetaInfo)
}

