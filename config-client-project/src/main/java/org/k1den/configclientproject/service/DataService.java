package org.k1den.configclientproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.k1den.configclientproject.entity.UserData;
import org.k1den.configclientproject.repository.UserDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataService {

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);
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
        logger.info("Получен запрос на сохранение данных: name={}, email={}", userData.getName(), userData.getEmail());

        UserData savedData = userDataRepository.save(userData);
        logger.info("Данные успешно сохранены в БД: {}", savedData);

        try {
            String jsonMessage = objectMapper.writeValueAsString(savedData);
            kafkaTemplate.send(kafkaTopic, String.valueOf(savedData.getId()), jsonMessage);
            logger.info("Сообщение отправлено в Kafka (топик='{}', key={}, payload={})", kafkaTopic, savedData.getId(), jsonMessage);
        } catch (JsonProcessingException e) {
            logger.error("Ошибка сериализации объекта в JSON: {}", e.getMessage(), e);
        }

        return savedData;
    }
}
