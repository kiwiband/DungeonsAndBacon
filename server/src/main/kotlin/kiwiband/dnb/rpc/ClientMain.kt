package kiwiband.dnb.rpc

import io.grpc.ManagedChannelBuilder

fun main() {
    val channel = ManagedChannelBuilder.forAddress("localhost", 12345).usePlaintext().build()
    val stub = SyncServiceGrpc.newBlockingStub(channel)
    val responce = stub.sync(Syncservice.Empty.getDefaultInstance())
    println(responce.str)
}