package digitaltwinframework.coreimplementation.restmanagement

import digitaltwinframework.coreimplementation.utils.eventbusutils.SystemEventBusAddresses
import digitaltwinframework.coreimplementation.utils.eventbusutils.messagecodec.SubrouterMessageCodec
import digitaltwinframework.coreimplementation.utils.eventbusutils.messagecodec.UnregisterSubrouterMessageCodec
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.EventBus
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

/**
 * This class constitute the REST server of a digital twin.
 * If multiple digital twins run on a single machine the REST server instance is one
 * and each digital twin register wich message want to receive from it
 * */
class RESTServer(val config: RESTServerConfig, val environmentName: String) : AbstractVerticle() {


    private lateinit var eb: EventBus

    private var subrouters = HashMap<String, ArrayList<Router>>()
    private var router = Router.router(this.vertx)

    private lateinit var server: HttpServer

    data class RegisterSubrouter(val handlerServiceId: String, val router: Router)
    data class UnregisterSubrouter(val handlerServiceId: String, val router: Router)

    override fun start() {
        super.start()
        this.eb = this.vertx.eventBus()
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
        this.eb.registerDefaultCodec(RegisterSubrouter::class.java, SubrouterMessageCodec())
        this.eb.registerDefaultCodec(UnregisterSubrouter::class.java, UnregisterSubrouterMessageCodec())
        println("RESTServer -> Message codec registered")
        this.eb.consumer<Any>(SystemEventBusAddresses.RESTServer.address) { message ->
            message.body().let {
                when (it) {
                    is RegisterSubrouter -> this.setSubRouter(it.handlerServiceId, it.router)
                    is UnregisterSubrouter -> this.removeSubRouter(it.handlerServiceId, it.router)
                }
            }
        }
    }

    fun setSubRouter(handlerServiceId: String, requestRouter: Router) {
        subrouters.getOrPut(handlerServiceId, { ArrayList() }).add(requestRouter)

        router.mountSubRouter("/", requestRouter)

        println("SUB ROUTER MOUNTED")
        router.routes.forEach { println("Available Route -> ${it.path}") }
    }

    fun removeSubRouter(handlerServiceId: String, requestRouter: Router) {
        subrouters.get(handlerServiceId)?.let {
            if (it.contains(requestRouter)) {
                requestRouter.routes
                        .map { route -> "/" + route.path }
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
