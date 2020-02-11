package hospitaldigitaltwins.ontologies

import com.fasterxml.jackson.annotation.JsonProperty
import digitaltwinframework.Model
import hospitaldigitaltwins.ontologies.procedures.Administration
import hospitaldigitaltwins.ontologies.procedures.Maneuver
import hospitaldigitaltwins.prehmanagement.ontologies.Complication
import hospitaldigitaltwins.prehmanagement.ontologies.PatientState

/**
 * Created by Matteo Gabellini on 2019-07-31.
 */
open class Patient(
    @JsonProperty("anagraphic") var anagraphic: Anagraphic = Anagraphic(),
    @JsonProperty("medicalHistory") var medicalHistory: MedicalHistory = MedicalHistory(),
    @JsonProperty("status") var status: PatientState = PatientState(),
    @JsonProperty("vitalParameters") var vitalParameters: ArrayList<VitalParameter<*>> = ArrayList(),
    @JsonProperty("executedManeuvers") var executedManeuvers: ArrayList<Maneuver> = ArrayList(),
    @JsonProperty("administrations") var administrations: ArrayList<Administration> = ArrayList(),
    @JsonProperty("complications") var complications: ArrayList<Complication> = ArrayList(),
    @JsonProperty("notes") var notes: ArrayList<Note<*>> = ArrayList()
) : Model