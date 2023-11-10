package com.example.orderservice.service;

import com.example.orderservice.entity.History;
import com.example.orderservice.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HistoryService {

    private final HistoryRepository historyRepository;

    public History createNewHistory(History history){
        log.info("Creating new history log");

        return historyRepository.save(history);
    }
}
