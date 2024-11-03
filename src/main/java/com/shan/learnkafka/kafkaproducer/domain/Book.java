package com.shan.learnkafka.kafkaproducer.domain;

public record Book(
        Integer bookId,
        String bookName,
        String bookAuthor
) {
}
