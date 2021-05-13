package base.strategy.utils.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("Request: обьект запроса расчета")
public class Request {
    @ApiModelProperty("Операнд 1")
    long op1;
    @ApiModelProperty("Операнд 2")
    long op2;
}

