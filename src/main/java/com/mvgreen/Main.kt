package com.mvgreen

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.http.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import main.java.com.mvgreen.GameModelImpl
import main.java.com.mvgreen.PlayerAction
import java.lang.Compiler.enable
import java.lang.Exception

val gameModel = GameModelImpl()

fun main(args: Array<String>) {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT) // Pretty Prints the JSON
            }
        }
        routing {
            get("/register") {
                val result = gameModel.addPlayer()
                call.respond(mapOf("id" to result))
            }
            get("/update") {
                val id: Int
                try {
                    id = call.request.queryParameters["id"]!!.toInt()
                } catch (e: Exception) {
                    call.respond("""{ "status" : "id request parameter not specified" }""")
                    return@get
                }

                val result = gameModel.getStatusForPlayer(id)
                if (result == null) {
                    call.respond("""{ "status" : "game was not started yet" }""")
                } else {
                    call.respond(result)
                }
            }
            get("/submit") {
                val id: Int
                val line: String
                try {
                    id = call.request.queryParameters["id"]!!.toInt()
                    line = call.request.queryParameters["line"]!!
                } catch (e: Exception) {
                    call.respond("""{ "status" : "invalid request" }""")
                    return@get
                }

                val success = gameModel.submitPlayerAction(PlayerAction(id, line))
                call.respond("""{ "status" : "${if (success) "OK" else "Invalid action"}" }""")
            }
        }
    }.start(wait = true)
}