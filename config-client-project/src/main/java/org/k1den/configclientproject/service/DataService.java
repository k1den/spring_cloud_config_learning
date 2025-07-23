package org.k1den.configclientproject.service;

import org.k1den.configclientproject.entity.UserData;
import org.k1den.configclientproject.repository.UserDataRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataService {

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String kafkaTopic;

    private final ObjectMapper objectMapper;

    public DataService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
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