package hospitaldigitaltwins.traumamanagement.ontologies

import hospitaldigitaltwins.ontologies.procedures.Procedure
import java.util.*

/**
 * Created by Matteo Gabellini on 03/02/2020.
 */
data class Diagnostics(override val name: String, override val executionTime: Date) : Procedure

enum class StrumentalDiagnostics(val stringFormat: String) {
    RX_TORACE("Rx Torace"),
    ECHOFAST("ECHOFAST"),
    RX_BACINO("Rx Bacino"),
    TC_CEREBRALE_CERVICALE("TC Cerebrale Cervicale"),
    TC_TORACO_ADDOMINALE("TC Toraco-addominale"),
    WB_TC("WB-TC");

    fun perform(executionTime: Date): Diagnostics {
        return Diagnostics(
            this.stringFormat,
            executionTime
        )
    }
}