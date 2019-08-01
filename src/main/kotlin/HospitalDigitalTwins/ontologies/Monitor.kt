package HospitalDigitalTwins.ontologies

/**
 * Created by Matteo Gabellini on 2019-08-01.
 */
class MonitorInfo(
    val physicalIdentifier: String,
    val manufacturer: String,
    val manufacturerGatewayInfo: ManufacturerGatewayInfo,
    val parameterMeasured: Array<VitalParameter<Any>>,
    var location: String
)

interface ManufacturerGatewayInfo