package app.utils;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Request {

    private String strEng;
    private String strRus;
    private BigDecimal val;
    private LocalDate date;


    public Request() {

    }
}
