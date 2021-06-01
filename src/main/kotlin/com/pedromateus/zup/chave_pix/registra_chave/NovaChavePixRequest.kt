package com.pedromateus.zup.chave_pix.registra_chave

import com.pedromateus.pix.RegistraCavePixRequest
import com.pedromateus.pix.TipoDeChave
import com.pedromateus.pix.TipoDeConta
import io.micronaut.core.annotation.Introspected
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
class NovaChavePixRequest(
    @field: NotBlank val tipoDeChaveRequest:TipoDeChaveRequest?,
    @field: Size(max=77) val chave:String?,
    @field: NotBlank  val tipoDeContaRequest: TipoDeContaRequest?
) {

    fun DtoParaRegistraChaveRequestGrpc(clienteId: UUID):RegistraCavePixRequest{
        return RegistraCavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoDeChave(tipoDeChaveRequest?.atributoGrpc?:TipoDeChave.UNKNOWN_TIPO_CHAVE)
            .setChave(chave)
            .setTipoDeConta(tipoDeContaRequest?.atributoGrpc?:TipoDeConta.UNKNOWN_TIPO_CONTA)
            .build()
    }

}

enum class TipoDeContaRequest(val atributoGrpc: TipoDeConta){
    CONTA_CORRENTE(TipoDeConta.CONTA_CORRENTE),

    CONTA_POUPANCA(TipoDeConta.CONTA_POUPANCA)
}

enum class TipoDeChaveRequest(val atributoGrpc:TipoDeChave){
    CPF(TipoDeChave.CPF),
    CELULAR(TipoDeChave.CELULAR),
    EMAIL(TipoDeChave.EMAIL),
    ALEATORIO(TipoDeChave.ALEATORIA)
}