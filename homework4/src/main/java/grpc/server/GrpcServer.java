package grpc.server;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(8081)
                .addService((BindableService) new PhoneServer()).build();
        server.start();
        System.out.println("Server started.");
        server.awaitTermination();

    }
}
