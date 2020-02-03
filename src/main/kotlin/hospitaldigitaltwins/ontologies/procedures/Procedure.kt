package hospitaldigitaltwins.ontologies.procedures

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */

/**
 *
 * @param executionTime
 * @param location
 */
interface Procedure {
    val name: String
    val executionTime: Date
}


interface TimedProcedure : Procedure {
    var endTime: Date?
}



