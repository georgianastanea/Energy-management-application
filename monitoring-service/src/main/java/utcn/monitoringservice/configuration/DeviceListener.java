package utcn.monitoringservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import utcn.monitoringservice.dto.DeviceDto;
import utcn.monitoringservice.entity.Device;
import utcn.monitoringservice.repository.DeviceRepository;

@Component
public class DeviceListener {

    private final DeviceRepository deviceRepository;

    public DeviceListener(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @RabbitListener(queues = MessageConfiguration.DEVICE_QUEUE)
    public void onDeviceReceived(String message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            DeviceDto deviceDto = objectMapper.readValue(message, DeviceDto.class);
            Device device = Device.builder()
                    .id(deviceDto.getId())
                    .maxHourlyEnergyConsumption(deviceDto.getMaxHourlyEnergyConsumption())
                    .build();

            deviceRepository.save(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
