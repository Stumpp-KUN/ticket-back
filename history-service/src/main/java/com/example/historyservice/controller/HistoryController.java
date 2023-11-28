package com.example.historyservice.controller;


import com.example.historyservice.entity.History;
import com.example.historyservice.service.impl.HistoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryServiceImpl historyService;

    @GetMapping("/collect/{ticketId}")
    public ResponseEntity<List<History>> getHistoryByIdTicket(@PathVariable Long ticketId) throws EntityNotFoundException {
        return ResponseEntity.ok(historyService.getAllHistory(ticketId));
    }
}

