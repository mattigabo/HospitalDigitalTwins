package digitaltwinframework.coreimplementation

import io.vertx.core.Vertx

/**
 * This class constitute the REST server of a digital twin.
 * If multiple digital twins run on a single machine the REST server instance is one
 * and each digital twin register wich message want to receive from it
 * */
class RESTServer private constructor() {
    private var server = Vertx.vertx().createHttpServer()

    companion object {
        private var INSTANCE: RESTServer? = null
        @JvmStatic
        fun getInstance(): RESTServer {
            if (INSTANCE == null) {
                INSTANCE = RESTServer()
            }
            return INSTANCE!!
        }
    }

    init {
        server.requestHandler({ request ->
            // This handler gets called for each request that arrives on the server
            var response = request.response()
            response.putHeader("content-type", "text/plain")

            // Write to the response and end it
            response.end("Hello World!")
        }).listen(8080, "localhost", { res ->
            if (res.succeeded()) {
                println("Server is now listening!")
            } else {
                println("Failed to bind!")
                println(res.cause())
            }
        })
    }
}
