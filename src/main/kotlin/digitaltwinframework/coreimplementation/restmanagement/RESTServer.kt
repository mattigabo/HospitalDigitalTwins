package digitaltwinframework.coreimplementation.restmanagement

import digitaltwinframework.coreimplementation.BasicDigitalTwinRunningEnvironment
import digitaltwinframework.coreimplementation.utils.eventbusutils.SystemEventBusAddresses
import digitaltwinframework.coreimplementation.utils.eventbusutils.messagecodec.DTRouterMessageCodec
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import java.net.URI

/**
 * This class constitute the REST server of a digital twin.
 * If multiple digital twins run on a single machine the REST server instance is one
 * and each digital twin register wich message want to receive from it
 * */
class RESTServer(val config: RESTServerConfig, val environmentName: String) : AbstractVerticle() {


    private val eb = BasicDigitalTwinRunningEnvironment.runningInstance!!.vertx.eventBus()

    private var digitalTwinRouters = HashMap<URI, ArrayList<Router>>()
    private var router = Router.router(this.vertx)

    private lateinit var server: HttpServer

    data class DTRouter(val digitalTwinID: URI, val router: Router)
    data class UnregisterRouter(val digitalTwinID: URI, val router: Router)

    override fun start() {
        super.start()

        server = this.vertx.createHttpServer()
        server.requestHandler(router).listen(config.portNumber, config.host) { res ->
            if (res.succeeded()) {
                println("${environmentName}: Rest Server is now listening!")
            } else {
                println("${environmentName}: Failed to bind the REST Server!")
                println(res.cause())
            }
        }

        this.registerToEventBus()
    }

    override fun stop() {
        super.stop()
        server.close()
    }


    private fun registerToEventBus() {
        this.eb.registerDefaultCodec(DTRouter::class.java, DTRouterMessageCodec())
        this.eb.consumer<Any>(SystemEventBusAddresses.RESTServer.address) { message ->
            message.body().let {
                when (it) {
                    is DTRouter -> this.setSubRouter(it.digitalTwinID, it.router)
                    is UnregisterRouter -> this.removeSubRouter(it.digitalTwinID, it.router)
                }
            }
        }
    }

    fun setSubRouter(digitalTwinID: URI, requestRouter: Router) {
        digitalTwinRouters.getOrPut(digitalTwinID, { ArrayList() }).add(requestRouter)

        router.mountSubRouter("/" + digitalTwinID.toString(), requestRouter)

        println("SUB ROUTER MOUNTED")
        router.routes.forEach { println("Available Route -> ${it.path}") }
    }

    fun removeSubRouter(digitalTwinID: URI, requestRouter: Router) {
        digitalTwinRouters.get(digitalTwinID)?.let {
            if (it.contains(requestRouter)) {
                requestRouter.routes
                        .map { route -> "/" + digitalTwinID.toString() + "/" + route.path }
                        .forEach { pathToDelete ->
                            router.routes
                                    .stream()
                                    .filter { route ->
                                        route.path != null && route.path.startsWith(pathToDelete)
                                    }.forEach { route -> route.remove() }
                        }
            }
        }
    }
}


data class RESTServerConfig(val host: String, val portNumber: Int)

object DevelopmentConfigurations {
    val localhostConfig = RESTServerConfig("localhost", 8080)
    val homePcConfig = RESTServerConfig("192.168.0.13", 8080)
    val basicConfig = RESTServerConfig("0.0.0.0", 8080)
}
