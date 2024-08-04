package utcn.monitoringservice.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import utcn.monitoringservice.entity.Device;
import utcn.monitoringservice.entity.HourlyConsumption;
import utcn.monitoringservice.entity.Measurement;
import utcn.monitoringservice.repository.DeviceRepository;
import utcn.monitoringservice.repository.HourlyConsumptionRepository;
import utcn.monitoringservice.repository.MeasurementRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final DeviceRepository deviceRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final HourlyConsumptionRepository hourlyConsumptionRepository;

    public MeasurementService(MeasurementRepository measurementRepository, DeviceRepository deviceRepository, SimpMessagingTemplate messagingTemplate, HourlyConsumptionRepository hourlyConsumptionRepository) {
        this.measurementRepository = measurementRepository;
        this.deviceRepository = deviceRepository;
        this.messagingTemplate = messagingTemplate;
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
    }

    public Map<LocalDateTime, Double> getHourlyEnergyConsumption(UUID deviceId) {
        List<Measurement> measurements = measurementRepository.findByDeviceId(deviceId);
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found!"));

        Map<LocalDateTime, Double> hourlyConsumptionMap = new HashMap<>();

        LocalDateTime minTimeStamp = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(measurements.get(0).getTimestamp()),
                ZoneId.systemDefault()
        );
        int index = 0;
        LocalDateTime startHour = minTimeStamp.withMinute(0).withSecond(0).withNano(0);

       for(int i=1; i< measurements.size(); i++){
           LocalDateTime measurementTimeStamp = LocalDateTime.ofInstant(
                   Instant.ofEpochMilli(measurements.get(i).getTimestamp()),
                   ZoneId.systemDefault()
           );
          if(measurementTimeStamp.getHour() - minTimeStamp.getHour() == 1 || measurementTimeStamp.getHour() - minTimeStamp.getHour() == -23){
              Double measurementValue = measurements.get(i).getMeasurementValue() - measurements.get(index).getMeasurementValue();

              hourlyConsumptionMap.put(startHour, measurementValue);
              hourlyConsumptionRepository.save(new HourlyConsumption(UUID.randomUUID(), deviceId, startHour, measurementValue));

              startHour = startHour.plusHours(1);
              minTimeStamp = measurementTimeStamp;
              index = i;

              if (measurementValue > device.getMaxHourlyEnergyConsumption()) {
                  sendMessageOverWebSocket("Measurement value exceeded device maximum energy consumption. Permitted: "
                          + device.getMaxHourlyEnergyConsumption() + " Actual: " + measurementValue);
              }

          }
       }
       if(!hourlyConsumptionMap.containsKey(startHour)){
           Double measurementValue = measurements.get(measurements.size()-1).getMeasurementValue() - measurements.get(index).getMeasurementValue();
           hourlyConsumptionMap.put(startHour, measurementValue);
           hourlyConsumptionRepository.save(new HourlyConsumption(UUID.randomUUID(), deviceId, startHour, measurementValue));
       }

        return hourlyConsumptionMap;
    }

    private void sendMessageOverWebSocket(String message) {
        messagingTemplate.convertAndSend("/topic/reply", message);
    }

}

