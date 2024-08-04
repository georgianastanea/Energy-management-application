package utcn.simulator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import utcn.simulator.SensorSimulator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

@Data
@NoArgsConstructor
@ToString
public class MeasurementDto {

    @JsonProperty("deviceId")
    private UUID deviceId;

    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("measurementValue")
    private Double measurementValue;

    public MeasurementDto(Long timestamp, Double measurementValue) {
        this.deviceId = SensorSimulator.idForSimulation;
        this.timestamp = timestamp;
        this.measurementValue = measurementValue;
    }
}
