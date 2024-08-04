package utcn.authentication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcn.authentication.services.CommunicationService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static utcn.authentication.services.CommunicationService.MONITORING_SERVICE_URL;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/measurements")
public class MeasurementsController {

    @GetMapping("/{deviceId}")
    public ResponseEntity<Map<LocalDateTime, Double>> getHourlyConsumption(@PathVariable UUID deviceId){
        return ResponseEntity.ok(CommunicationService.sendGetMapRequest(MONITORING_SERVICE_URL + "measurements/" + deviceId));
    }
}
