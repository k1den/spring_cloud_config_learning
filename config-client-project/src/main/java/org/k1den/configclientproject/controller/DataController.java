package org.k1den.configclientproject.controller;

import org.k1den.configclientproject.entity.UserData;
import org.k1den.configclientproject.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;

    @Value("${app.message}")
    private String configMessage;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.port}")
    private String serverPort;

    @PostMapping("/data")
    public ResponseEntity<UserData> createUserData(@RequestBody UserData userData) {
        UserData savedData = dataService.saveUserData(userData);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfigInfo() {
        Map<String, String> configInfo = new HashMap<>();
        configInfo.put("message", configMessage);
        configInfo.put("applicationName", applicationName);
        configInfo.put("serverPort", serverPort);
        configInfo.put("status", "Конфиг успешно загружен с сервака");
        return ResponseEntity.ok(configInfo);
    }
}
