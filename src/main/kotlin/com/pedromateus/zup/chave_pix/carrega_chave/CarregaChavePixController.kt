package com.pedromateus.zup.chave_pix.carrega_chave

import com.pedromateus.pix.BuscaChavePixRequest
import com.pedromateus.pix.BuscaUmaChavePiServiceGrpc
import com.pedromateus.pix.ListaChavePixRequest
import com.pedromateus.pix.ListaChavesPixDeUmClienteGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class CarregaChavePixController(
    private val carregaChaveClient: BuscaUmaChavePiServiceGrpc.BuscaUmaChavePiServiceBlockingStub,
    private val listaChavesClient: ListaChavesPixDeUmClienteGrpc.ListaChavesPixDeUmClienteBlockingStub
) {

    private val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Get("/pix/{pixId}")
    fun carregaChavePix(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        LOGGER.info("Cliente com id $clienteId buscando chave de id $pixId")
        val responseGrpc = carregaChaveClient.buscaUmachavePix(
            BuscaChavePixRequest.newBuilder()
                .setPixId(
                    BuscaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setChavePix(pixId.toString())
                        .setClienteID(clienteId.toString())
                        .build()
                )
                .build()
        )


        return HttpResponse.ok(DetalhesPixResponse(responseGrpc))

    }

    @Get("/pix")
    fun carregaTodasAsChavesDeUmUsuario(clienteId:UUID):HttpResponse<Any>{

        val response=listaChavesClient.listaChavePixService(ListaChavePixRequest.newBuilder().setClienteId(clienteId.toString()).build())

        val chaves=response.chavesList.map {
            ChavePixResponse(it)
        }

        return HttpResponse.ok(ClienteESuasChaves(response.clienteId,chaves))
    }
}