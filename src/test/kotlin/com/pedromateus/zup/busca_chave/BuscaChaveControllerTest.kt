package com.pedromateus.zup.busca_chave

import com.google.protobuf.Timestamp
import com.pedromateus.pix.*
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)
class BuscaChaveControllerTest{

    @field:Inject
    lateinit var buscaChaveClient:BuscaUmaChavePiServiceGrpc.BuscaUmaChavePiServiceBlockingStub
    @field:Inject
    @field:Client("/")
    lateinit var client:HttpClient

    @Test
    fun `deve retornar os dados de consulta de uma chave`(){
        val clienteId= UUID.randomUUID()
        val pixId=UUID.randomUUID()

        given(buscaChaveClient.buscaUmachavePix(any())).willReturn(retornaDadosDaChave(clienteId = clienteId.toString(),pixId = pixId.toString()))

        val responseHttp = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(responseHttp, Any::class.java)

        with(response) {
            assertEquals(HttpStatus.OK,status)
            assertNotNull(body())
        }

    }

    private fun retornaDadosDaChave(clienteId:String,pixId:String)=
        BuscaChavePixResponse.newBuilder()
            .setClientId(clienteId)
            .setPixId(pixId)
            .setChave(BuscaChavePixResponse.ChavePix.newBuilder()
                .setTipoDechave(TipoDeChave.EMAIL)
                .setChave("teste@teste.com")
                .setConta(BuscaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(TipoDeConta.CONTA_CORRENTE)
                    .setNumeroDeConta("0123456789")
                    .setCpfDoTitular("999.000.999-00")
                    .setInstituicao("Banco Itau")
                    .setAgencia("04321")
                    .build())
                .setCriadoEm(LocalDateTime.now().run{
                    val criadoEm=this.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setNanos(criadoEm.nano)
                        .setSeconds(criadoEm.epochSecond)
                        .build()
                })
                .build())
            .build()


    @Replaces(bean = BuscaUmaChavePiServiceGrpc.BuscaUmaChavePiServiceBlockingStub::class)
    @Singleton
    fun removeChavePix() = mock(BuscaUmaChavePiServiceGrpc.BuscaUmaChavePiServiceBlockingStub::class.java)
}