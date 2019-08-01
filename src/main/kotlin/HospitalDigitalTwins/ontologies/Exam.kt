package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class Exam(val name: String, var result: ExamResult, val startTime: Date, var endTime: Date)

interface ExamResult