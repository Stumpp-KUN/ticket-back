package com.example.orderservice.service;

import com.example.orderservice.entity.History;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HistoryService {

    private final HistoryRepository historyRepository;

    public History createNewHistory(History history) {
        log.info("Creating new history log");

        return historyRepository.save(history);
    }

    public List<History> getAllHistory(Long ticketId) throws EntityNotFoundException {
        log.info("Finding all history");

        List<History> histories = historyRepository.findAllByTicketId(ticketId);
        if (histories.isEmpty()) throw new EntityNotFoundException("No history to this ticket");
        Collections.sort(histories, Comparator.comparing(History::getDate).reversed());
        return histories;
    }
}
