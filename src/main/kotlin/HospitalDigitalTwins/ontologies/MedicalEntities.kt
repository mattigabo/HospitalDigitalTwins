package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
open class MedicalOperator(open var anagraphic: Anagraphic, open var specialization: String)

class MedicalTeamMember(override var anagraphic: Anagraphic, override var specialization: String, var role: String) : MedicalOperator(anagraphic, specialization)

class MedicalTeam(val formationTime: Date, val members: Array<MedicalTeamMember>)

enum class Specializations {
    DOCTOR, NURSE
}