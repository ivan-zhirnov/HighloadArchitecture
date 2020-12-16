package grpc.server;


import grpc.PhoneServiceGrpc;
import grpc.PhoneServiceOuterClass.*;
import grpc.client.service.PhoneClientService;
import io.grpc.stub.StreamObserver;

public class PhoneServer extends PhoneServiceGrpc.PhoneServiceImplBase {

    @Override
    public void checkPhone(PhoneRequest request, StreamObserver<PhoneResponse> responseObserver) {
        PhoneResponse response = PhoneResponse.newBuilder().setMessage("Hello" + request.getPhone()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
