package hospitaldigitaltwins.prehmanagement.patients

object PatientRESTHandlers {
    val GET_PATIENT = "getPatientInfo"

    val SET_MEDICAL_HISTORY = "setMedicalHistory"
    val GET_MEDICAL_HISTORY = "getMedicalHistory"

    val UPDATE_ANAGRAPHIC = "updateAnagraphic"
    val GET_ANAGRAPHIC = "getAnagraphic"

    val UPDATE_STATUS = "updateStatus"
    val GET_STATUS = "getStatus"


    val GET_VITALPARAMETERS_HISTORY = "getVitalParametersHistory"
    val ADD_VITALPARAMETERS = "addVitalParameters"
    val GET_VITALPARAMETERS = "getCurrentVitalParameters"

    val GET_VITALPARAMETER = "getVitalParameter"

    val GET_ALL_DRUGS = "getAdministrations"
    val ADD_DRUG = "addAdministration"

    val GET_EXECUTED_MANEUVERS = "getExecutedManeuvers"
    val ADD_MANEUVER = "addManeuver"

    val ADD_TIMED_MANEUVER = "addTimedManeuver"
    val UPDATE_TIMED_MANEUVER = "updateTimedManeuver"
    val GET_TIMED_MANEUVER = "getTimedManeuver"

    val GET_ALL_COMPLICATIONS = "getAllComplications"
    val ADD_COMPLICATION = "addComplication"
    val GET_COMPLICATION = "getComplication"

    val GET_ALL_NOTES = "getAllNotes"
    val ADD_TEXT_NOTE = "addTextNote"
    val ADD_AUDIO_NOTE = "addAudioNote"
    val ADD_PHOTO_NOTE = "addPhoto"
    val ADD_VIDEO_NOTE = "addVideo"

    val GET_TEXT_NOTE = "getTextNote"
    val GET_AUDIO_NOTE = "getAudioNote"
    val GET_PHOTO_NOTE = "getPhotos"
    val GET_VIDEO_NOTE = "getVideos"
}