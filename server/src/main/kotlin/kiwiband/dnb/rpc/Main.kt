package kiwiband.dnb.rpc

import io.grpc.ServerBuilder


fun main() {
    val syncService = SyncServiceImpl()
    ServerBuilder.forPort(12345).addService(syncService).build().start()
}
