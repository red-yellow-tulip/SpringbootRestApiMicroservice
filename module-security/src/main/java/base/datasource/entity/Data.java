package base.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
public class Data {

    @Getter @Setter
    private String d1;
    @Getter @Setter
    private String d2;

    public Data() {

    }
}
