package hospitaldigitaltwins.traumamanagement.patient

import digitaltwinframework.InteractionAdapter

class PatientRESTFaceAdapter(thisDT: PatientDT) : InteractionAdapter

object OperationIDs {
    const val UPDATE_ANAGRAPHIC = "updateAnagraphic"
    const val GET_ANAGRAPHIC = "getAnagraphic"
    const val GET_CLINICAL_DATA = "getClinicalData"
    const val UPDATE_CLINICAL_DATA = "updateClinicalData"
    const val GET_ALL_VITAL_PARAMETERS = "getAllVitalParameters"
    const val ADD_NEW_VITAL_PARAMETER_VALUE = "addNewVitalParameterValues"
    const val GET_LAST_VITAL_PARAMETERS = "getLastVitalParameters"
    const val GET_ALL_SPECIFIED_VITAL_PARAMETER_VALUES = "getAllSpecifiedVitalParameterValues"
    const val GET_LAST_SPECIFIED_VITAL_PARAMETER_VALUE = "getLastSpecifiedVitalParameterValue"
    const val GET_ALL_TREATMENTS = "getAllTreatments"
    const val ADD_TREATMENT = "addTreatment"
    const val ADD_ADMINISTRATION = "addAdministration"
    const val ADD_MANEUVER = "addManeuver"
    const val GET_COMPLICATIONS = "getComplications"
    const val ADD_COMPLICATION = "addComplication"
    const val GET_COMPLICATION_TIME = "getComplicationTime"
    const val DELETE_COMPLICATION = "deleteComplication"
    const val ADD_NOTE = "addNote"
    const val GET_NOTES = "getNotes"
    const val DELETE_NOTE = "deleteNote"
    const val GET_MULTIMEDIA = "getMultimedia"
    const val ADD_MULTIMEDIA = "addMultimedia"
    const val DELETE_MULTIMEDIA = "deleteMultimedia"
}