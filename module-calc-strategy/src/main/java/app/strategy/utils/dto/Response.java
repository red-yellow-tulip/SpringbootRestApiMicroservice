package app.strategy.utils.dto;


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
@ApiModel("Response: обьект с результатами расчетов")
public class Response {
    @ApiModelProperty("Результат операции вычисления")
    long result;
}
