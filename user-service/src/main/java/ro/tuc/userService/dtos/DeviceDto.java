package ro.tuc.userService.dtos;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceDto {

    private UUID id;
    private String description;
    private String address;
    private Double maxHourlyEnergyConsumption;
    private UUID personId;
}
