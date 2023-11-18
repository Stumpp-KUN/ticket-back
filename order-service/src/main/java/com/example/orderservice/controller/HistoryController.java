package com.example.orderservice.controller;

import com.example.orderservice.entity.History;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/collect/{ticketId}")
    public ResponseEntity<List<History>> getHistoryByIdTicket(@PathVariable Long ticketId) throws EntityNotFoundException {
        return ResponseEntity.ok(historyService.getAllHistory(ticketId));
    }
}
