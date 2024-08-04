package ro.tuc.ds2023.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2023.dtos.DeviceDto;
import ro.tuc.ds2023.services.DeviceService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable UUID id){
        return ResponseEntity.ok(deviceService.getDevice(id));
    }

    @GetMapping
    public List<DeviceDto> getAllDevices(){
        return deviceService.getAllDevices();
    }

    @PostMapping
    public ResponseEntity<DeviceDto> addDevice(@RequestBody DeviceDto deviceDto) throws JsonProcessingException {
        return ResponseEntity.ok(deviceService.addDevice(deviceDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(@PathVariable UUID id, @RequestBody DeviceDto deviceDto) throws JsonProcessingException {
        return ResponseEntity.ok(deviceService.updateDevice(id, deviceDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id){
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<List<DeviceDto>> getDevicesByPersonId(@PathVariable UUID id){
        return ResponseEntity.ok(deviceService.getDevicesByPersonId(id));
    }
}
