package app.controller.mapper;

import app.controller.dto.RequestDTO;
import app.controller.dto.ResponseDTO;
import app.controller.utils.Request;
import app.controller.utils.Response;
import org.springframework.stereotype.Component;

@Component
public class Mapper {


    public Request toRequest(RequestDTO requestTO) {
        return Request.builder()
                .date(requestTO.getDate())
                .strEng(requestTO.getStrEng())
                .strRus(requestTO.getStrRus())
                .val(requestTO.getVal())
                .build();
    }

    public ResponseDTO toResponseDTO(Response response) {
        return  ResponseDTO.builder()
                .date(response.getDate())
                .strEng(response.getStrEng())
                .strRus(response.getStrRus())
                .val(response.getVal())
                .build();
    }
}
