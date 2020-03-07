package hospitaldigitaltwins.common

import digitaltwinframework.coreimplementation.utils.eventbusutils.FailureCode
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import io.vertx.kotlin.core.json.get

/**
 * Created by Matteo Gabellini on 05/03/2020.
 */
abstract class AbstractMongoClientService(mongoConfigPath: String) {
    lateinit var mongoClient: MongoClient
    abstract var collection: String

    protected val emptySearchQuery = JsonObject()


    protected fun <T> executeDistinctQuery(fieldName: String, destinationClass: Class<T>): Future<T> {
        var promise: Promise<T> = Promise.promise()
        mongoClient.distinctWithQuery(collection, fieldName, JsonObject::class.java.name, emptySearchQuery) { res ->
            when {
                res.succeeded() -> {
                    var jobjRes = res.result().get<JsonObject>(0)
                    var result = jobjRes.mapTo(destinationClass)
                    promise.complete(result)
                }
                else -> {
                    println(res.cause())
                    promise.fail(res.cause())
                }
            }
        }
        return promise.future()
    }

    protected fun updateField(fieldName: String, newField: JsonObject): Future<String> {
        val update = JsonObject()
        update.put(fieldName, newField)
        return this.performUpdate(update)
    }

    protected fun <T> updateField(fieldName: String, newField: T): Future<String> {
        val update = JsonObject()
        update.put(fieldName, newField)
        return this.performUpdate(update)
    }

    private fun performUpdate(newField: JsonObject): Future<String> {
        var promise: Promise<String> = Promise.promise()
        var update = JsonObject()
        update.put("\$set", newField)
        mongoClient.updateCollection(collection, emptySearchQuery, update) { res ->
            when {
                res.succeeded() -> promise.complete("Update complete!")
                res.failed() -> promise.fail(res.cause())
                else -> promise.fail("Error during update")
            }
        }
        return promise.future()
    }


    protected fun <T> onOperationCompleteHandler(message: Message<JsonObject>): Handler<AsyncResult<T>> {
        return Handler<AsyncResult<T>> { ar: AsyncResult<T> ->
            when {
                ar.succeeded() -> message.reply(JsonObject.mapFrom(ar.result()))
                else -> message.fail(FailureCode.PROBLEM_WITH_MONGODB, ar.cause().toString())
            }
        }
    }
}