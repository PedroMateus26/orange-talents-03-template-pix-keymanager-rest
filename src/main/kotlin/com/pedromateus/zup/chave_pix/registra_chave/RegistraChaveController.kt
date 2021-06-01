package com.pedromateus.zup.chave_pix.registra_chave


import com.pedromateus.pix.RegistraChavePixServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import org.slf4j.LoggerFactory
import java.util.*

@Validated
@Controller("/api/v1/clientes/{clienteId}")
class RegistraChaveController(private val registraChavePixClient : RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub) {

    private val LOGGER=LoggerFactory.getLogger(this::class.java)

    @Post("/pix")
    fun registraChavePix(clienteId:UUID, @Body request:NovaChavePixRequest):HttpResponse<Any>{

        LOGGER.info("[$clienteId] criando uma nova chave pix com request $request")
        val grpcResponse=registraChavePixClient.registraChavePix(request.DtoParaRegistraChaveRequestGrpc(clienteId))

        return HttpResponse.created(location(clienteId,grpcResponse.chavePix))

    }

    private fun location(clienteId:UUID, chavePix:String)=HttpResponse
        .uri("api/v1/clientes/$clienteId/pix/${chavePix}")
}