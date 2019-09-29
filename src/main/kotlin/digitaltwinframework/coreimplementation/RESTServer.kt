package digitaltwinframework.coreimplementation

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import java.net.URI

/**
 * This class constitute the REST server of a digital twin.
 * If multiple digital twins run on a single machine the REST server instance is one
 * and each digital twin register wich message want to receive from it
 * */
class RESTServer : AbstractVerticle() {

    private var eb = BasicDigitalTwinSystem.RUNNING_INSTANCE!!.vertx.eventBus()

    private var host = "localhost"
    private var portNumber = 8080

    private var dtSystemSuffix = "/digitaltwinsystem"
    private var digitalTwinRouters = HashMap<URI, Router>()
    private var router = Router.router(this.vertx)

    private lateinit var server: HttpServer

    data class NewRouter(val digitalTwinID: URI, val router: Router)
    data class UnregisterRouter(val digitalTwinID: URI)

    override fun start() {
        super.start()

        server = Vertx.vertx().createHttpServer()

        router.route().path("/prova").handler { routingContext ->
            // This handler gets called for each request that arrives on the server
            val response = routingContext.response()
            response.putHeader("content-type", "text/plain")

            // Write to the response and end it
            response.end("Hello World!")
        }

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
        this.eb.consumer<Any>(DTSystemEventBusAddresses.RESTServer.address) { message ->
            message.body().let {
                when (it) {
                    is NewRouter -> this.setSubRouter(it.digitalTwinID, it.router)
                    is UnregisterRouter -> this.removeSubRouter(it.digitalTwinID)
                }
            }
        }
    }

    fun setSubRouter(digitalTwinID: URI, requestRouter: Router) {
        digitalTwinRouters.put(digitalTwinID, requestRouter)
        router.mountSubRouter("/" + digitalTwinID.toString(), requestRouter)
    }

    fun removeSubRouter(digitalTwinID: URI) {
        router.routes
                .stream()
                .filter { route -> route.path != null && route.path.startsWith("/" + digitalTwinID.toString()) }
                .forEach { route -> route.remove() }
        digitalTwinRouters.remove(digitalTwinID)
    }
}


