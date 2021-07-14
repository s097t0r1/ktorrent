package metainfo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TorrentMetaInfo(

    @SerialName("announce")
    val announce: String,

    @SerialName("info")
    val info: Info
)

@Serializable
class Info(

    @SerialName("piece length")
    val pieceLength: Int,

    @SerialName("pieces")
    val pieces: ByteArray
)
