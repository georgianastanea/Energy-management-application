package utcn.simulator.service;

import utcn.simulator.dto.MeasurementDto;

import java.io.InputStream;
import java.util.*;

public class SensorService {

    private static final long DELAY_BETWEEN_MEASUREMENTS = 10 * 60 * 1000;

    private static int addMinutes = 0;

    public static List<MeasurementDto> getMeasurements() {
        List<MeasurementDto> measurements = new ArrayList<>();

        try (InputStream inputStream = SensorService.class.getClassLoader().getResourceAsStream("sensor.csv")) {
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    double val = Double.parseDouble(line);

                    Long timestamp = System.currentTimeMillis() + (DELAY_BETWEEN_MEASUREMENTS * addMinutes);

                    measurements.add(new MeasurementDto(timestamp, val));
                    addMinutes++;
                }
                scanner.close();
            } else {
                System.out.println("File not found: sensor.csv");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return measurements;
    }
}