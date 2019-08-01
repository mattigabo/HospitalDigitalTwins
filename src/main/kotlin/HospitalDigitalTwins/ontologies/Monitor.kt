package HospitalDigitalTwins.ontologies

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class MonitorInfo(
    val physicalIdentifier: String,
    val manufacturer: String,
    val manufacturerGatewayInfo,
    val parameterMeasured: Array<VitalParameter>,
    var location: String
)