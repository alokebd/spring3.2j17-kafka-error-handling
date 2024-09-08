package com.java2tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.java2tech.dto.User;
import com.java2tech.publisher.KafkaMessagePublisher;
import com.java2tech.util.CsvReaderUtils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/producer")
public class EventController {

    @Autowired
    private KafkaMessagePublisher publisher;


    @PostMapping("/publish/users")
    public ResponseEntity<?> publishEvent(@RequestBody User user) {
        try {
            publisher.sendEvents(user);
            log.info("Controller: request {}", user);
            return ResponseEntity.ok("Message published successfully");
        } catch (Exception exception) {
            return ResponseEntity.
                    status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
    

    @PostMapping("/publish/csv")
    public ResponseEntity<?> uploadCsv() {
        try {
            List<User> users = CsvReaderUtils.readDataFromCsv();
            users.forEach(usr -> publisher.sendEvents(usr));
            return ResponseEntity.ok("Message published successfully");
        } catch (Exception exception) {
            return ResponseEntity.
                    status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
