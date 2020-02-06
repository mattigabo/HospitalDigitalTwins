package hospitaldigitaltwins.ontologies

import digitaltwinframework.Model

/**
 * Created by Matteo Gabellini on 2019-07-31.
 */
class Patient(
    var anagraphic: Anagraphic,
    var medicalHistory: MedicalHistory,
    //var vitalParameter: List<VitalParameter>,
    var notes: Notes
) : Model