package ro.tuc.userService.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.userService.services.CommunicationService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/measurements")
public class MeasurementsController {

    @GetMapping("/{deviceId}")
    public ResponseEntity<Map<LocalDateTime, Double>> getHourlyConsumption(@PathVariable UUID deviceId){
        return ResponseEntity.ok(CommunicationService.sendGetMapRequest("http://monitoring-service:8082/measurements/" + deviceId));
    }
}
