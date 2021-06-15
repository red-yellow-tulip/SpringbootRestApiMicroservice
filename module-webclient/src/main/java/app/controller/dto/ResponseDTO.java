package app.controller.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Data
public class ResponseDTO {
    private String strEng;
    private String strRus;
    private BigDecimal val;
    private LocalDate date;

    public ResponseDTO() {

    }
}
