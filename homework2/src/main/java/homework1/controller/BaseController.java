package homework1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "v1")
public class BaseController {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public void handleException(Exception e,
                                HttpServletResponse response) {
        e.printStackTrace();
        writeErrorToResponse(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void writeErrorToResponse(HttpServletResponse response, HttpStatus status) {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ObjectMapper jsonMapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            map.put("result", status);
            jsonMapper.writeValue(out, map);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
