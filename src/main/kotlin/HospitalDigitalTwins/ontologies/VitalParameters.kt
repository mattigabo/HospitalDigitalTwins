package HospitalDigitalTwins.ontologies

import java.util.*

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
open class VitalParameter<T>(open var value: T, open var acquisitionTime: Date)

class RespiratoryTract(override var value: RespiratoryTractValue, override var acquisitionTime: Date) : VitalParameter<RespiratoryTractValue>(value, acquisitionTime)

enum class RespiratoryTractValue {
    OPEN, CLOSED
}

class BreathingRate(override var value: Int, override var acquisitionTime: Date) : VitalParameter<Int>(value, acquisitionTime)


class OutlyingSaturation(override var value: Int, override var acquisitionTime: Date) : VitalParameter<Int>(value, acquisitionTime)