package utcn.monitoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MeasurementDto implements Serializable{

    private UUID deviceId;
    private Long timestamp;
    private Double measurementValue;
}
