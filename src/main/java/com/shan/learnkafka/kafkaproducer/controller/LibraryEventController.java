package com.shan.learnkafka.kafkaproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shan.learnkafka.kafkaproducer.domain.LibraryEvent;
import com.shan.learnkafka.kafkaproducer.domain.LibraryEventType;
import com.shan.learnkafka.kafkaproducer.producer.LibraryEventProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/api/v1/library")
public class LibraryEventController {

    private final LibraryEventProducer libraryEventProducer;

    public LibraryEventController(LibraryEventProducer libraryEventProducer) {
        this.libraryEventProducer = libraryEventProducer;
    }

    @PostMapping("/bookevent")
    public ResponseEntity<LibraryEvent> create(@RequestBody  @Valid LibraryEvent le) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        log.info("Before Library event to be sent - {}", le);
        libraryEventProducer.sendLibraryEvent_Approach2(le);
        log.info("After Library event sent - {}", le);
        return ResponseEntity.status(HttpStatus.CREATED).body(le);
    }
    @GetMapping("/hello")
    public String hello() {

        return "Hello World";
    }

    @PutMapping("/bookevent")
    public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
        ResponseEntity<String> re = validateLibraryEvent(libraryEvent);
        if(re != null){
          return re;
        }
        libraryEventProducer.sendLibraryEvent_Approach2(libraryEvent);
        log.info("after produce call");
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

    private ResponseEntity<String> validateLibraryEvent(@Valid LibraryEvent libraryEvent) {
        if(libraryEvent.libraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
        }
        if(!libraryEvent.libraryEventType().equals(LibraryEventType.UPDATE)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only UPDATE event type is supported");
        }
        return null;
    }

}

