package com.pedromateus.zup.chave_pix.remove_chave

import com.pedromateus.pix.RemoveChavePixServiceGrpc
import com.pedromateus.pix.UsuarioPixRemoveRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import org.slf4j.LoggerFactory
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class RemovendoChavePixController (private val removeGrpcClent:RemoveChavePixServiceGrpc.RemoveChavePixServiceBlockingStub){

   val LOGGER = LoggerFactory.getLogger(this::class.java)

    @Delete("/pix/{pixId}")
    fun removeChavePix( @PathVariable clienteId:UUID, pixId:UUID):HttpResponse<Any>{

        LOGGER.info("$clienteId removendo chave $pixId")

        removeGrpcClent.removendoChavePix(UsuarioPixRemoveRequest.newBuilder()
            .setClienteID(clienteId.toString())
            .setChavePix(pixId.toString())
            .build())

        return HttpResponse.ok()

    }
}
