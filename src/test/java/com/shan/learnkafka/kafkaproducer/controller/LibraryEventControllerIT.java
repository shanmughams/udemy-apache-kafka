package com.shan.learnkafka.kafkaproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shan.learnkafka.kafkaproducer.controller.util.TestUtil;
import com.shan.learnkafka.kafkaproducer.domain.LibraryEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = {"library-events"}, partitions = 3)
@TestPropertySource(properties = {"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
class LibraryEventControllerIT {

    @Autowired
    TestRestTemplate restTemplate;

    /*
        1 - configure embedded kafka broker
        2 - Override broker  bootstrap address to the embeddedKafkaBroker
        3 - configure kafka consumer
        4 - wire kafka consumer and  embeddedKafkaBroker
        5 - consume the record from embeddedKafkaBroker and assert
     */

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<Integer, String> consumer;

    @BeforeEach
    void setup(){

        var config = new HashMap<>(KafkaTestUtils.consumerProps("group1",
                "true",embeddedKafkaBroker));
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");
        consumer = new DefaultKafkaConsumerFactory<>(config, new IntegerDeserializer(),
                new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void postLibraryEvent() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON.toString());
        var httpEntity = new HttpEntity<>(TestUtil.libraryEventRecord(), httpHeaders);
        var response = restTemplate.exchange("/api/v1/library/bookevent", HttpMethod.POST, httpEntity, LibraryEvent.class);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        ConsumerRecords<Integer, String> records = KafkaTestUtils.getRecords(consumer);
        assert records.count() == 1;
        records.forEach(a -> {
            var le = TestUtil.parseLibraryEventRecord(objectMapper, a.value());
            System.out.println(le);
            assertEquals(le, TestUtil.libraryEventRecord());
        });
    }

    @Test
    public void putEvent() throws JsonProcessingException {
        LibraryEvent libraryEvent = TestUtil.libraryEventRecordUpdate();
        System.out.println("le " + objectMapper.writeValueAsString(libraryEvent));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("content-type", MediaType.APPLICATION_JSON.toString());
        HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent,httpHeaders);

        ResponseEntity<LibraryEvent> re = restTemplate.exchange("/api/v1/library/bookevent",
                HttpMethod.PUT, request, LibraryEvent.class);
        assertEquals(HttpStatus.OK, re.getStatusCode());

        ConsumerRecords<Integer, String> records = KafkaTestUtils.getRecords(consumer);
        assert records.count() == 1;
        records.forEach(a -> {
            if(a.key() != null) {
                var libraryEventActual = TestUtil.parseLibraryEventRecord(objectMapper, a.value());
                assertEquals(libraryEvent, libraryEventActual);
            }
        });

    }
}