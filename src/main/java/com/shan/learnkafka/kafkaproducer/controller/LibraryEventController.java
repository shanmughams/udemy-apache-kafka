package com.shan.learnkafka.kafkaproducer.controller;

import com.shan.learnkafka.kafkaproducer.domain.LibraryEvent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Slf4j
@RestController
@RequestMapping("/api/v1/library")
public class LibraryEventController {
    @PostMapping("/bookevent")
    public ResponseEntity<LibraryEvent> create(@RequestBody LibraryEvent le) {
//        log.info("Body - {}", le);
        return ResponseEntity.status(HttpStatus.CREATED).body(le);
    }
    @GetMapping("/hello")
    public String hello() {

        return "Hello World";
    }
}
