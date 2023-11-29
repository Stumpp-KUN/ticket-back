package com.example.orderservice.exception;

public class KafkaException extends Exception{
    public KafkaException(String message) {
        super(message);
    }
}
