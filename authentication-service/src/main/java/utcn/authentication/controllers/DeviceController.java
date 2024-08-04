package utcn.authentication.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcn.authentication.dtos.DeviceDto;
import utcn.authentication.services.CommunicationService;

import java.util.List;
import java.util.UUID;

import static utcn.authentication.services.CommunicationService.DEVICE_SERVICE_URL;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/device")
public class DeviceController {

    @GetMapping("/{id}")
    public ResponseEntity<DeviceDto> getDevice(@PathVariable UUID id){
        return ResponseEntity.ok(CommunicationService
                .sendGetByCriteriaRequest(DeviceDto.class, DEVICE_SERVICE_URL + "device/" + id));
    }

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getAllDevices(){
        return ResponseEntity.ok(CommunicationService
                .sendGetAllRequest( DEVICE_SERVICE_URL + "device", DeviceDto[].class));
    }

    @PostMapping
    public ResponseEntity<Object> addDevice(@RequestBody DeviceDto deviceDto){
        Object response = CommunicationService
                .sendPostRequest(deviceDto, DEVICE_SERVICE_URL + "device");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDevice(@PathVariable UUID id, @RequestBody DeviceDto deviceDto){
        Object response = CommunicationService
                .sendPutRequest(deviceDto, DEVICE_SERVICE_URL + "device/" + id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable UUID id){
        CommunicationService.sendDeleteRequest( DEVICE_SERVICE_URL + "device/" + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<List<DeviceDto>> getDevicesByPersonId(@PathVariable UUID id){
        return ResponseEntity.ok(CommunicationService
                .sendGetAllRequest( DEVICE_SERVICE_URL + "device/person/" + id, DeviceDto[].class));
    }
}
