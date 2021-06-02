package app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class Address implements Serializable {
    private String country;
    private String city;
    private String street;
    private Long homeNumber;
    private Long flatNumber;

    public Address() {
    }
}
