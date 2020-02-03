package hospitaldigitaltwins.prehmanagement.patients

import digitaltwinframework.coreimplementation.restmanagement.AbstractRESTInteractionAdapter

class RESTRoutingAdapter : AbstractRESTInteractionAdapter()

object PatientOperationIDS {
    val ADD_PATIENT = "addPatient"
    val GET_PATIENTS = "getAllPatients"

    val UPDATE_ANAGRAPHIC = "updateAnagraphic"
    val GET_ANAGRAPHIC = "getAnagraphic"

    val UPDATE_STATUS = "updateStatus"
    val GET_STATUS = "getStatus"

    val ADD_VITALPARAMETERS = "addVitalParameters"
    val GET_VITALPARAMETERS = "getAllVitalParameters"
    val UPDATE_VITALPARAMETER = "updateVitalParameter"
    val GET_VITALPARAMETER = "getVitalParameter"
    val DEL_VITALPARAMETER = "delVitalParameter"
    val GET_LAST_VITALPARAMETERS = "getLastVitalParameters"

    val GET_ALL_DRUGS = "getDrugs"
    val ADD_DRUG = "addDrug"
    val GET_DRUG = "getDrug"
    val DEL_DRUG = "delDrug"

    val GET_ALL_MANEUVERS = "getAllManeuvers"
    val ADD_SIMPLE_MANEUVER = "addSimpleManuver"
    val GET_SIMPLE_MANEUVER = "getSimpleManeuver"
    val DEL_SIMPLE_MANEUVER = "delSimpleManeuver"
    val ADD_PACING_MANEUVER = "addPacingManeuver"
    val GET_PACING_MANEUVER = "getPacingManeuver"
    val DEL_PACING_MANEUVER = "delPacingManeuver"

    val GET_ALL_TREATMENTS = "getAllTreatments"
    val ADD_SIMPLE_TREATMENT = "addSimpleTreatment"
    val GET_SIMPLE_TREATMENT = "getSimpleTreatment"
    val DEL_SIMPLE_TREATMENT = "delSimpleTreatment"
    val ADD_TIMED_TREATMENT = "addTimedTreatment"
    val GET_TIMED_TREATMENT = "getTimedTreatment"
    val DEL_TIMED_TREATMENT = "delTimedTreatment"
    val ADD_INJECTION = "addInjectionTreatment"
    val GET_INJECTION = "getInjectionTreatment"
    val DEL_INJECTION = "delInjectionTreatment"
    val ADD_IPPV = "addIppvTreatment"
    val GET_IPPV = "getIppvTreatment"
    val DEL_IPPV = "delIppvTreatment"

    val GET_ALL_COMPLICATIONS = "getAllComplications"
    val ADD_COMPLICATION = "addComplication"
    val GET_COMPLICATION = "getComplication"
    val DEL_COMPLICATION = "delComplication"

    val GET_ALL_NOTES = "getAllNotes"
    val UPDATE_NOTES = "updateNotes"

    val GET_ALL_MULTIMEDIA = "getAllMultimedia"
    val ADD_MULTIMEDIA = "addMultimedia"
    val GET_MULTIMEDIA = "getMultimedia"
    val DEL_MULTIMEDIA = "delMultimedia"
}