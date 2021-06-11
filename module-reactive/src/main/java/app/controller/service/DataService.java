package app.controller.service;

import app.controller.utils.Request;
import app.controller.utils.Response;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class DataService {

    public Response execute(Request request) {

        return Response.builder()
                .date(request.getDate().plusYears(1))
                .strEng(request.getStrEng().toUpperCase())
                .strRus(request.getStrRus().toUpperCase())
                .val(request.getVal().multiply(BigDecimal.valueOf(2)))
                .build();
    }

    public Response execute(String id) {
        return Response.builder()
                .date(LocalDate.now().plusYears(Integer.parseInt(id)))
                .strEng(id)
                .strRus(id)
                .val(BigDecimal.valueOf(Long.parseLong(id)))
                .build();
    }
}
