package utcn.authentication.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommunicationService {

    public static final String USER_SERVICE_URL = "http://user-service:8080/";
    public static final String DEVICE_SERVICE_URL = "http://device-service:8081/";
    public static final String MONITORING_SERVICE_URL = "http://monitoring-service:8082/";
    public static final String CHAT_SERVICE_URL = "http://chat-service:8085/";

    public static Object sendPostRequest(Object data, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(data);
        return restTemplate.postForEntity(url, request, data.getClass());
    }

    public static void sendDeleteRequest(String url){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }

    public static <T> List<T> sendGetAllRequest(String url, Class<T[]> responseType) {
        ResponseEntity<T[]> response = new RestTemplate().getForEntity(url, responseType);
        T[] dataArray = response.getBody();

        if (dataArray != null) {
            return Arrays.asList(dataArray);
        } else {
            return Collections.emptyList();
        }
    }

    public static <T> T sendGetByCriteriaRequest(Class<T> responseType, String url){
        return new RestTemplate().getForObject(url, responseType);
    }

    public static Object sendPutRequest(Object data, String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> request = new HttpEntity<>(data);
        return restTemplate.exchange(url, HttpMethod.PUT, request, data.getClass());
    }

    public static Map sendGetMapRequest(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, Map.class);
    }
}