package ro.tuc.userService.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private UUID id;
    private String username;
    private String password;
    private String role;
}
