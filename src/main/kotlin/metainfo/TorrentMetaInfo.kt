package metainfo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TorrentMetaInfo(

    @SerialName("announce")
    val announce: String = "",

    @SerialName("info")
    val info: Info,

)

@Serializable
class Info(

    @SerialName("piece length")
    val pieceLength: Int,

    @SerialName("pieces")
    val pieces: ByteArray,

    @SerialName("name")
    val name: String,

    @SerialName("length")
    val length: Int? = null,

    @SerialName("files")
    val files: List<File>? = null

)

@Serializable
class File(

    @SerialName("length")
    val length: Int,

    @SerialName("md5sum")
    val md5sum: String? = null,

    @SerialName("path")
    val path: String

)
