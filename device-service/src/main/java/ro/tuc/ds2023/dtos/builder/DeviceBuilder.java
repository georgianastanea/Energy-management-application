package ro.tuc.ds2023.dtos.builder;

import lombok.NoArgsConstructor;
import ro.tuc.ds2023.dtos.DeviceDto;
import ro.tuc.ds2023.entities.Device;
import ro.tuc.ds2023.entities.Person;

@NoArgsConstructor
public class DeviceBuilder {

    public static DeviceDto toDeviceDto(Device device) {
        return DeviceDto.builder()
                .id(device.getId())
                .description(device.getDescription())
                .address(device.getAddress())
                .maxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption())
                .personId(device.getPerson().getId())
                .build();
    }

    public static Device toDevice(DeviceDto deviceDto, Person client) {
        return Device.builder()
                .id(deviceDto.getId())
                .description(deviceDto.getDescription())
                .address(deviceDto.getAddress())
                .maxHourlyEnergyConsumption(deviceDto.getMaxHourlyEnergyConsumption())
                .person(client)
                .build();
    }
}
