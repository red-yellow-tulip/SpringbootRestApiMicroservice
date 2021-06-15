package app.mapper;

import app.utils.dto.RequestDTO;
import app.utils.dto.ResponseDTO;
import app.utils.Request;
import app.utils.Response;
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
