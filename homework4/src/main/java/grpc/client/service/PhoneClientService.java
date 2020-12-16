package grpc.client.service;

import java.util.Map;

public interface PhoneClientService {
    Map<String, Object> checkPhone(String phone, String code);
}
