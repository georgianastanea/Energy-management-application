package utcn.monitoringservice.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    private UUID id;
    private String description;
    private String address;
    private Double maxHourlyEnergyConsumption;
    private UUID personId;
}
