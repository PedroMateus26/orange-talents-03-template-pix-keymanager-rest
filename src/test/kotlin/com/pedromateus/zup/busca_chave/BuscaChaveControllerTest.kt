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
import java.time.Instant
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
    lateinit var listaDeChavesClient:ListaChavesPixDeUmClienteGrpc.ListaChavesPixDeUmClienteBlockingStub

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

    @Test
    fun `deve retornar uma lista`(){
        val clienteId=UUID.randomUUID()
        given(listaDeChavesClient.listaChavePixService(any())).willReturn(listaChavePixResponse(clienteId = clienteId.toString()))

        val request=HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix")
        val response=client.toBlocking().exchange(request,List::class.java)

        with(response){
            assertEquals(HttpStatus.OK,status)
            assertNotNull(body())
            assertEquals(body().size,1)
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

    private fun listaChavePixResponse(clienteId:String): ListaChavePixResponse? {
        val chaveEmail=ListaChavePixResponse.Chave.newBuilder()
            .setTipo(TipoDeChave.EMAIL)
            .setChave("teste@teste.com")
            .setTipoDeConta(TipoDeConta.CONTA_CORRENTE)
            .setPixId(UUID.randomUUID().toString())
            .setCriadoEm(
                LocalDateTime.now().run{
                    val criadoEm=this.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setNanos(criadoEm.nano)
                        .setSeconds(criadoEm.epochSecond)
                        .build()
                })
            .build()
        return ListaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChaves(listOf(chaveEmail))
            .build()
    }


    @Replaces(bean = BuscaUmaChavePiServiceGrpc.BuscaUmaChavePiServiceBlockingStub::class)
    @Singleton
    fun removeChavePix() = mock(BuscaUmaChavePiServiceGrpc.BuscaUmaChavePiServiceBlockingStub::class.java)

    @Replaces(bean = ListaChavesPixDeUmClienteGrpc.ListaChavesPixDeUmClienteBlockingStub::class)
    @Singleton
    fun listaDeChaves()=mock(ListaChavesPixDeUmClienteGrpc.ListaChavesPixDeUmClienteBlockingStub::class.java)
}