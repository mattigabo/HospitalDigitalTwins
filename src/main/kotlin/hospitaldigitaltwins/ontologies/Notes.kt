package hospitaldigitaltwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
interface Note<T> {
    val content: T
    val generationTime: Date
}

data class TextNote(override val content: String, override val generationTime: Date) : Note<String>
data class AudioNote(override val content: Byte, override val generationTime: Date) : Note<Byte>
data class PhotoNote(override val content: Byte, override val generationTime: Date) : Note<Byte>
data class VideoNote(override val content: Byte, override val generationTime: Date) : Note<Byte>