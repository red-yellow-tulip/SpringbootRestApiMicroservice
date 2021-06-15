package app.utils.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Data
public class ResponseDTO implements Serializable {
    private String strEng;
    private String strRus;
    private BigDecimal val;
    private LocalDate date;

    public ResponseDTO() {

    }
}
