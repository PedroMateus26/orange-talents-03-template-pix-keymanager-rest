package com.pedromateus.zup.registra_chave

import com.pedromateus.pix.RegistraChavePixServiceGrpc
import com.pedromateus.pix.RegitraChavePixResponse
import com.pedromateus.zup.chave_pix.registra_chave.NovaChavePixRequest
import com.pedromateus.zup.chave_pix.registra_chave.TipoDeChaveRequest
import com.pedromateus.zup.chave_pix.registra_chave.TipoDeContaRequest
import com.pedromateus.zup.chave_pix.shared.grpc.GrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class RegistraChaveControllerTest {

    @field:Inject
    lateinit var registraStub:RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
   internal fun `deve registrar uma chave pix`(){
        //preparar cenário
        val clienteId=UUID.randomUUID()
        val chavePixID=UUID.randomUUID()

        val grpcResponse= RegitraChavePixResponse.newBuilder()
            .setClienteID(clienteId.toString())
            .setChavePix(chavePixID.toString())
            .build()

        given(registraStub.registraChavePix(Mockito.any())).willReturn(grpcResponse)

        val novaChavePix=NovaChavePixRequest(
            tipoDeContaRequest = TipoDeContaRequest.CONTA_CORRENTE,
            tipoDeChaveRequest = TipoDeChaveRequest.EMAIL,
            chave = "teste@email"
        )

        val request = HttpRequest.POST("api/v1/clientes/$clienteId/pix",novaChavePix)
        val response=client.toBlocking().exchange(request,NovaChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED,response.status)

    }

    @Factory
    @Replaces(factory = GrpcFactory::class)
    internal class MockitoStubFactory{
        @Singleton
        fun stubMock()= Mockito.mock(RegistraChavePixServiceGrpc.RegistraChavePixServiceBlockingStub::class.java)
    }
}