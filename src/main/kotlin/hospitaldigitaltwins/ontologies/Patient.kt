package hospitaldigitaltwins.ontologies

import com.fasterxml.jackson.annotation.JsonProperty
import digitaltwinframework.Model
import hospitaldigitaltwins.prehmanagement.ontologies.Complication
import hospitaldigitaltwins.prehmanagement.ontologies.PatientState
import io.vertx.core.json.JsonArray

/**
 * Created by Matteo Gabellini on 2019-07-31.
 */
open class Patient(
    @JsonProperty("anagraphic") var anagraphic: Anagraphic = Anagraphic(),
    @JsonProperty("medicalHistory") var medicalHistory: MedicalHistory = MedicalHistory(),
    @JsonProperty("status") var status: PatientState = PatientState(),
    @JsonProperty("vitalParameters") var vitalParameters: JsonArray = JsonArray(),
    @JsonProperty("executedManeuvers") var executedManeuvers: JsonArray = JsonArray(),
    @JsonProperty("administrations") var administrations: JsonArray = JsonArray(),
    @JsonProperty("complications") var complications: ArrayList<Complication> = ArrayList(),
    @JsonProperty("notes") var notes: ArrayList<Note<*>> = ArrayList()
) : Model

open class MongoPatient(
    @JsonProperty("anagraphic") var anagraphic: Anagraphic = Anagraphic(),
    @JsonProperty("medicalHistory") var medicalHistory: MedicalHistory = MedicalHistory(),
    @JsonProperty("status") var status: PatientState = PatientState()
)