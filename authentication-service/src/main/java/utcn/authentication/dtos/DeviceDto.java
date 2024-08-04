package utcn.authentication.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
