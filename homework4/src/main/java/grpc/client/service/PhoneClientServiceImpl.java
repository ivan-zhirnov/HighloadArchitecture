package grpc.client.service;

import grpc.PhoneServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import grpc.PhoneServiceOuterClass.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class PhoneClientServiceImpl implements PhoneClientService {

    private final PhoneServiceGrpc.PhoneServiceBlockingStub blockingStub;

    PhoneClientServiceImpl() {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localHost:8081").usePlaintext().build();
        this.blockingStub = PhoneServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Map<String, Object> checkPhone(String phone, String code) {
        PhoneRequest request = PhoneRequest.newBuilder().setPhone(phone).setCode(code).build();
        PhoneResponse response = blockingStub.checkPhone(request);
        Map<String, Object> result = new HashMap<>();
        result.put("message", response.getMessage());
        result.put("responseCode", response.getResponseCode());
        return result;
    }
}
