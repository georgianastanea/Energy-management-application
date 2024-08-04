package utcn.monitoringservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import utcn.monitoringservice.dto.MeasurementDto;
import utcn.monitoringservice.entity.Measurement;
import utcn.monitoringservice.repository.MeasurementRepository;

import java.util.UUID;

@Component
public class MeasurementsListener {

    private final MeasurementRepository measurementRepository;

    public MeasurementsListener(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @RabbitListener(queues = MessageConfiguration.MEASUREMENTS_QUEUE)
    public void onMeasurementsMessageReceived(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MeasurementDto measurementDto = objectMapper.readValue(message, MeasurementDto.class);

            System.out.println("Received message: " + measurementDto.toString());

            Measurement measurement = Measurement.builder()
                    .id(UUID.randomUUID())
                    .deviceId(measurementDto.getDeviceId())
                    .timestamp(measurementDto.getTimestamp())
                    .measurementValue(measurementDto.getMeasurementValue())
                    .build();
            measurementRepository.save(measurement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
