package com.pedromateus.zup.chave_pix.shared.grpc

import com.pedromateus.pix.BuscaUmaChavePiServiceGrpc
import com.pedromateus.pix.ListaChavesPixDeUmClienteGrpc
import com.pedromateus.pix.RegistraChavePixServiceGrpc
import com.pedromateus.pix.RemoveChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcFactory (@GrpcChannel(value = "keyManager") val channel:ManagedChannel){

    @Singleton
    fun registraChave()=RegistraChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave()=RemoveChavePixServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun buscaChave()=BuscaUmaChavePiServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves()=ListaChavesPixDeUmClienteGrpc.newBlockingStub(channel)


}