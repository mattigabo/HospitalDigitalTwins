package digitaltwinframework

/**
 * The class that implements this interface will represents the execution environment
 * where the digital twin is running (e.g. the process that execute the digital twin)
 * */
interface DigitalTwinExecutor {
    fun executeDigitalTwin(factory: DigitalTwinFactory)
}