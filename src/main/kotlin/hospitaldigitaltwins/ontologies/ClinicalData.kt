package hospitaldigitaltwins.ontologies

import hospitaldigitaltwins.ontologies.treatments.Treatment

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class ClinicalData(var vitalParameters: MutableList<VitalParameter<Any>>, var treatments: MutableList<Treatment>)