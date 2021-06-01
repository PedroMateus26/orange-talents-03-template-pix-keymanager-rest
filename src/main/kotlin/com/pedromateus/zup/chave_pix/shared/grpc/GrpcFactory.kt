package com.pedromateus.zup.chave_pix.shared.grpc

import com.pedromateus.pix.RegistraChavePixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcFactory (@GrpcChannel(value = "keyManager") val channel:ManagedChannel){

    @Singleton
    fun registraChave()=RegistraChavePixServiceGrpc.newBlockingStub(channel)


}