package com.pedromateus.zup.handler

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import io.micronaut.http.server.exceptions.ExceptionHandler
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class GlobalExceptionHandler: ExceptionHandler<StatusRuntimeException, HttpResponse<Any>> {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>?, exception: StatusRuntimeException?): HttpResponse<Any> {

        val statusCode = exception!!.status.code
        val statusDescription = exception.status.description

        val (httpStatus, message) = when (statusCode){
            Status.NOT_FOUND.code->Pair(HttpStatus.NOT_FOUND,statusDescription)
            Status.INVALID_ARGUMENT.code->Pair(HttpStatus.BAD_REQUEST, "Dados de entradas inválidos!")
            Status.ALREADY_EXISTS.code->Pair(HttpStatus.UNPROCESSABLE_ENTITY,"Esses dados já estão registradlseo na base de dados! Não é permitido registro duplicado.")
            else->{
                LOGGER.error("Erro inesperado!")
                Pair(HttpStatus.I_AM_A_TEAPOT, "Deu rui demais malandro, corre para as montanhas que só Deus saber que erro deu.$statusDescription")
            }
        }

        return HttpResponse.status<JsonError>(httpStatus)
            .body(JsonError(message))

    }
}