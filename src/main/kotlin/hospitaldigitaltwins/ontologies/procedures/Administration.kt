package hospitaldigitaltwins.ontologies.procedures

import java.util.*

/**
 * Created by Matteo Gabellini on 03/02/2020.
 */
open class Administration(val drug: String, val executionTime: Date) {
    val name: String = "Administration of ${this.drug}"
}

class VariableAdministration(drug: String, executionTime: Date, val dosage: Int) : Administration(drug, executionTime)
