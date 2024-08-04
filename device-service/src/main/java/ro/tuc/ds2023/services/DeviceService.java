package ro.tuc.ds2023.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2023.configuration.ConfigFileWriter;
import ro.tuc.ds2023.configuration.MessageConfiguration;
import ro.tuc.ds2023.dtos.DeviceDto;
import ro.tuc.ds2023.dtos.builder.DeviceBuilder;
import ro.tuc.ds2023.entities.Device;
import ro.tuc.ds2023.entities.Person;
import ro.tuc.ds2023.repositories.DeviceRepository;
import ro.tuc.ds2023.repositories.PersonRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final DeviceRepository deviceRepository;
    private final PersonRepository personRepository;

    public DeviceService(DeviceRepository deviceRepository, PersonRepository personRepository) {
        this.deviceRepository = deviceRepository;
        this.personRepository = personRepository;
    }

    public DeviceDto getDevice(UUID id){
        Device device = deviceRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException("Device not found!");
        });
        return DeviceBuilder.toDeviceDto(device);
    }

    public List<DeviceDto> getAllDevices(){
        List<Device> devices = deviceRepository.findAll();
        return devices.stream().map(DeviceBuilder::toDeviceDto).collect(Collectors.toList());
    }

    public DeviceDto addDevice(DeviceDto deviceDto) throws JsonProcessingException {
        Person person = personRepository.findById(deviceDto.getPersonId())
                .orElseThrow(() -> new RuntimeException("Device owner does not exist!"));

        Device device = DeviceBuilder.toDevice(deviceDto, person);
        deviceRepository.save(device);

        // Create config file for device simulation
        ConfigFileWriter.writeToFile(device.getId().toString(), "{\n    \"deviceId\": \"" + device.getId().toString() + "\"\n}");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(DeviceBuilder.toDeviceDto(device));
        rabbitTemplate.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.DEVICE_ROUTING_KEY, jsonPayload);

        return DeviceBuilder.toDeviceDto(device);
    }

    public DeviceDto updateDevice(UUID id, DeviceDto newDevice) throws JsonProcessingException {
        if(!deviceRepository.existsById(id)){
            throw new RuntimeException("Device does not exist!");
        }
        Person person = personRepository.findById(newDevice.getPersonId())
                .orElseThrow(() -> new RuntimeException("Device owner does not exist!"));

        Device device = DeviceBuilder.toDevice(newDevice, person);
        device.setId(id);
        deviceRepository.save(device);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(DeviceBuilder.toDeviceDto(device));
        rabbitTemplate.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.DEVICE_ROUTING_KEY, jsonPayload);

        return DeviceBuilder.toDeviceDto(device);
    }

    public void deleteDevice(UUID id){
        if(!deviceRepository.existsById(id)){
            throw new RuntimeException("Device does not exist!");
        }
        deviceRepository.deleteById(id);
    }

    public List<DeviceDto> getDevicesByPersonId(UUID id){
        List<Device> devices = deviceRepository.findByPerson_Id(id);
        return devices.stream().map(DeviceBuilder::toDeviceDto).collect(Collectors.toList());
    }
}
