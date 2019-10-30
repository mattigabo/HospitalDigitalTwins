package hospitaldigitaltwins.ontologies

import java.util.*

interface VitalParameter<T> {
    val value: T
    val acquisitionTime: Date
}
