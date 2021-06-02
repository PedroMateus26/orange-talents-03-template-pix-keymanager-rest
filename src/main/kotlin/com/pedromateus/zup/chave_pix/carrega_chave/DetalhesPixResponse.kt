package com.pedromateus.zup.chave_pix.carrega_chave

import com.pedromateus.pix.BuscaChavePixResponse
import com.pedromateus.pix.ListaChavePixResponse
import com.pedromateus.pix.TipoDeConta
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class DetalhesPixResponse(buscaUmaChavePixResponse: BuscaChavePixResponse) {

    val pixId = buscaUmaChavePixResponse.pixId
    val tipo = buscaUmaChavePixResponse.chave.tipoDechave
    val chave = buscaUmaChavePixResponse.chave.chave

    val criadoem = buscaUmaChavePixResponse.chave.criadoEm.run {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoDeConta = when (buscaUmaChavePixResponse.chave.conta.tipo) {
        TipoDeConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        TipoDeConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "Tipo de conta inexistente!"
    }

    val conta = mapOf(
        Pair("Tipo de conta ", tipoDeConta),
        Pair("Nomde do titular: ", buscaUmaChavePixResponse.chave.conta.nomeDoTitular),
        Pair("Cpf do titular", buscaUmaChavePixResponse.chave.conta.cpfDoTitular),
        Pair("Agência: ", buscaUmaChavePixResponse.chave.conta.agencia),
        Pair("Número da conta: ", buscaUmaChavePixResponse.chave.conta.numeroDeConta),
    )


}

class ChavePixResponse(chavePixResponse: ListaChavePixResponse.Chave){
    val pixId=chavePixResponse.pixId
    val tipoDeConta=chavePixResponse.tipoDeConta
    val tipoDeChave=chavePixResponse.tipo
    val chave=chavePixResponse.chave
    val criadoEm=chavePixResponse.criadoEm.run {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds,nanos.toLong()), ZoneId.of("UTC"))
    }
}

class ClienteESuasChaves(
    val clienteId:String,
    val chaves:List<ChavePixResponse>
)