package HospitalDigitalTwins.ontologies.treatments

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class Manouvers(val name: String, executionTime: Date, location: String) : BasicTreatment(executionTime, location)