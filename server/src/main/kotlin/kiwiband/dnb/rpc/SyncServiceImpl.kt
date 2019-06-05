package kiwiband.dnb.rpc

import io.grpc.stub.StreamObserver

class SyncServiceImpl: SyncServiceGrpc.SyncServiceImplBase() {

    override fun sync(request: Syncservice.Empty, responseObserver: StreamObserver<Syncservice.String>) {
        println("Hello, i'm server")
        responseObserver.onNext(Syncservice.String.newBuilder().setStr("Hello!").build())
        responseObserver.onCompleted()
    }
}
