package com.pedromateus.zup.remove_chave

import com.pedromateus.pix.RemoveChavePixServiceGrpc
import com.pedromateus.pix.UsuarioPixRemoveRequest
import com.pedromateus.pix.UsuarioPixRemoveResponse
import com.pedromateus.zup.chave_pix.shared.grpc.GrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
internal class RemoveChavePixControllerTest {

    @field:Inject
    lateinit var removeStub: RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    @BeforeEach
    fun resetBeffore() {
        Mockito.reset(removeStub)
    }

    @Test
    fun `deve removar uma chave pix existente`() {

        //preparar o cenário
        val clienteID = UUID.randomUUID()
        val pixId = UUID.randomUUID()

        val responseGrpc = UsuarioPixRemoveResponse.newBuilder()
            .setMessage("Chave removida com sucesso!")
            .build()

        given(removeStub.removendoChavePix(any())).willReturn(responseGrpc)

        val responseHttp = HttpRequest.DELETE<Any>("/api/v1/clientes/$clienteID/pix/$pixId")
        val response = client.toBlocking().exchange(responseHttp, Any::class.java)

//          assertNull(response.body())
        assertEquals(HttpStatus.OK,response.status)

    }


    @Replaces(bean = RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub::class)
    @Singleton
    fun removeChavePix() = mock(RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub::class.java)

}