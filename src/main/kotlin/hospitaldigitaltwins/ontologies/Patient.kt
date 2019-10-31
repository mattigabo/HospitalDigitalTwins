package hospitaldigitaltwins.ontologies

import digitaltwinframework.PhysicalAssetDataModel
import java.util.*

/**
 * Created by Matteo Gabellini on 2019-07-31.
 */
class PatientDataModel(
    val id: UUID,
    val creation: Date,
    var anagraphic: Anagraphic,
    var clinicalData: ClinicalData
) : PhysicalAssetDataModel