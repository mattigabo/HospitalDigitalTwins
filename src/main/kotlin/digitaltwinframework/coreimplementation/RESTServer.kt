package digitaltwinframework.coreimplementation

import digitaltwinframework.coreimplementation.messagecodec.DTRouterMessageCodec
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.net.URI

/**
 * This class constitute the REST server of a digital twin.
 * If multiple digital twins run on a single machine the REST server instance is one
 * and each digital twin register wich message want to receive from it
 * */
class RESTServer(val dtSystem: BasicDigitalTwinSystem) : AbstractVerticle() {

    private var eb = BasicDigitalTwinSystem.RUNNING_INSTANCE!!.vertx.eventBus()

    //private var host = "localhost"
    private var host = "192.168.0.13"
    private var portNumber = 8080

    private var dtSystemSuffix = "/digitaltwinsystem"
    private var digitalTwinRouters = HashMap<URI, ArrayList<Router>>()
    private var router = Router.router(this.vertx)

    private var infoRequestHandler = { routingContext: RoutingContext ->
        val response = routingContext.response()
        response.putHeader("content-type", "text/json")

        val responseBody = """
            {
                "digitalTwinsystem":{
                    "name":${this.dtSystem.name}
                    "RunningDigitalTwin": ${this.dtSystem.runningDT.size}
                }
            }
        """.trimIndent()
        response.end(responseBody)
    }

    private lateinit var server: HttpServer

    data class DTRouter(val digitalTwinID: URI, val router: Router)
    data class UnregisterRouter(val digitalTwinID: URI, val router: Router)

    override fun start() {
        super.start()

        server = this.vertx.createHttpServer()

        router.route().path("/info").handler(infoRequestHandler)

        server.requestHandler(router).listen(portNumber, host) { res ->
            if (res.succeeded()) {
                println("Digital Twin System: Server is now listening!")
            } else {
                println("Digital Twin System: Failed to bind the REST Server!")
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


