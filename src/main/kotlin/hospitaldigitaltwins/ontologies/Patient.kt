package hospitaldigitaltwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-07-31.
 */
class Patient(
    val id: UUID,
    val creation: Date,
    var anagraphic: Anagraphic,
    var clinicalData: ClinicalData
)