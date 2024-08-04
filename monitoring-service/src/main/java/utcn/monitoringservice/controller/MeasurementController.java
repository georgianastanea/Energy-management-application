package utcn.monitoringservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utcn.monitoringservice.service.MeasurementService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @GetMapping("/{deviceId}")
    private ResponseEntity<Map<LocalDateTime, Double>> computeTotalHourlyEnergyConsumption(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(measurementService.getHourlyEnergyConsumption(deviceId));
    }
}
