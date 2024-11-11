package com.shan.learnkafka.kafkaproducer.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record LibraryEvent(
        Integer libraryEventId,
        @NotNull
        LibraryEventType libraryEventType,
        @NotNull @Valid
        Book book
) {
}
