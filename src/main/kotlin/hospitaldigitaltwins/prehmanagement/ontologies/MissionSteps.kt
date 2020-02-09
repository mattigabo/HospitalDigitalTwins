package hospitaldigitaltwins.prehmanagement.ontologies

import java.time.LocalDateTime

/**
 * Created by Matteo Gabellini on 03/02/2020.
 */
enum class MissionSteps(val text: String) {
    DEPATURE_FROM_HOSPITAL("Crew departure from hospital"),
    ARRIVAL_ON_SITE("Crew arrived on emergency site"),
    DEPARTURE_FROM_SITE("Crew departure from site"),
    ARRIVAL_IN_HOSPITAL("Crew arrived at the hospital");

    override fun toString(): String {
        return this.text
    }

    /**
     * Factory method that create a Tracking step instance with the step text from which the method was invoked
     * */
    fun occurs(occurrenceTime: LocalDateTime): TrackingStep {
        return TrackingStep(
                this.text,
                occurrenceTime
        )
    }
}

class TrackingStep(val stepText: String, occurenceTime: LocalDateTime)
