package com.pedromateus.zup.exception_handler

import com.pedromateus.zup.handler.GlobalExceptionHandler
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val requestGenerica = HttpRequest.GET<Any>("/")

    @Test
    internal fun `deve retornar 404 quando status exception for not found`() {
        val mensagem = "não encontrado"
        val notFoundException = StatusRuntimeException(Status.NOT_FOUND.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.NOT_FOUND, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 400 quando status exception for bad request`() {
        val mensagem = "Dados de entradas inválidos!"
        val notFoundException = StatusRuntimeException(Status.INVALID_ARGUMENT.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.BAD_REQUEST, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    internal fun `deve retornar 422 quando status exception for unprocessable entity`() {
        val mensagem = "Esses dados já estão registradlseo na base de dados! Não é permitido registro duplicado."
        val notFoundException = StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }

    @Test
    internal fun `Quando o erro for desconhecido`() {
        val mensagem =
            "Deu rui demais malandro, corre para as montanhas que só Deus saber que erro deu."
        val notFoundException = StatusRuntimeException(Status.UNKNOWN.withDescription(mensagem))

        val resposta = GlobalExceptionHandler().handle(requestGenerica, notFoundException)

        assertEquals(HttpStatus.I_AM_A_TEAPOT, resposta.status)
        assertNotNull(resposta.body())
        assertEquals(mensagem, (resposta.body() as JsonError).message)
    }
}