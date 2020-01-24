package digitaltwinframework.coreimplementation.utils.eventbusutils

/**
 * The Set of the core  element adresses of the digital twin framework implementation, usable with the vert.x event bus
 * */
enum class SystemEventBusAddresses(val address: String) {
    RESTServer("RestServer"),
    SEPARATOR("."),
    EVOLUTION_CONTROLLER_SUFFIX("evolutionController");

    companion object {
        /**
         * return the concatenation of prefix, separator and suffix
         * */
        fun composeAddress(prefix: String, suffix: String): String {
            return prefix + SEPARATOR + suffix
        }
    }

    fun preappend(prefix: String): String {
        return composeAddress(prefix, this.address)
    }

    override fun toString(): String {
        return this.address
    }
}
