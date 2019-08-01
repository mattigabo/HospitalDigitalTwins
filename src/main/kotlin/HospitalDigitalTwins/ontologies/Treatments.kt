package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */

/**
 *
 * @param executionTime
 * @param location
 */
data class Treatment(val executionTime: Date, val location: kotlin.String? = null)