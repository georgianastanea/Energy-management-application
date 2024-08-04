package utcn.simulator;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import utcn.simulator.dto.MeasurementDto;
import utcn.simulator.service.SensorService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableScheduling
public class SensorSimulator {

    public static UUID idForSimulation;
    public static List<MeasurementDto> measurements;

    public static void getDataFromConfigFile(String path){
        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));

            JSONObject jsonObject = new JSONObject(content);
            String deviceId = jsonObject.getString("deviceId");

            idForSimulation = UUID.fromString(deviceId);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String port = args[0];
        System.setProperty("server.port", port);

        getDataFromConfigFile("../src/main/resources/" + args[1]);
        measurements = SensorService.getMeasurements();

        SpringApplication.run(SensorSimulator.class, args);
    }
}