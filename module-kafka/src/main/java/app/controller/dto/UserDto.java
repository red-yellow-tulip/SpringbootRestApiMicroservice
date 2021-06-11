package app.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private Long age;
    private String name;
    private Address address;

    public UserDto() {
    }
}