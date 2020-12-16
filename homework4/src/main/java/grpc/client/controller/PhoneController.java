package grpc.client.controller;

import grpc.client.service.PhoneClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class PhoneController {

    @Autowired
    private PhoneClientService phoneClientService;


    @RequestMapping(value = "checkPhone")
    @ResponseBody
    public Map<String, Object> checkPhone(@RequestParam(value = "phone") String phone,
                                          @RequestParam(value = "code") String code) {
        return phoneClientService.checkPhone(phone, code);
    }


}
