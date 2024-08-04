package utcn.simulator.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utcn.simulator.SensorSimulator;
import utcn.simulator.dto.MeasurementDto;
import java.util.Date;

@Component
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 1000)
    public void sendMessage() throws JsonProcessingException {
        MeasurementDto measurement = SensorSimulator.measurements.get(0);
        String jsonPayload = objectMapper.writeValueAsString(measurement);

        System.out.println("Sending message..." + new Date(measurement.getTimestamp()).getHours() + " " + new Date(measurement.getTimestamp()).getMinutes() + " " + " value " + measurement.getMeasurementValue() + "\n");
        rabbitTemplate.convertAndSend(MessageConfiguration.EXCHANGE, MessageConfiguration.ROUTING_KEY, jsonPayload);
        SensorSimulator.measurements.remove(0);
    }
}
