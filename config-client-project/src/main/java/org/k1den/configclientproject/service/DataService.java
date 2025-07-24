package org.k1den.configclientproject.service;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.k1den.configclientproject.entity.UserData;
import org.k1den.configclientproject.repository.UserDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
public class DataService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    private final ObjectMapper objectMapper;

    public DataService() {
        this.objectMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        this.objectMapper.registerModule(module);
    }

    public UserData saveUserData(UserData userData) {
        UserData savedData = userDataRepository.save(userData);
        System.out.println("Сохранено в бд: " + savedData);

        try {
            String jsonMessage = objectMapper.writeValueAsString(savedData);
            kafkaTemplate.send(kafkaTopic, String.valueOf(savedData.getId()), jsonMessage);
            System.out.println("Отправлено в топик кафки '" + kafkaTopic + "': " + jsonMessage);
        } catch (JsonProcessingException e) {
            System.err.println("Ошибка преобразования в JSON: " + e.getMessage());
        }

        return savedData;
    }
}