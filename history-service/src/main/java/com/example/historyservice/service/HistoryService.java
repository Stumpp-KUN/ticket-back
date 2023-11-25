package com.example.historyservice.service;

import com.example.historyservice.entity.History;
import com.example.historyservice.event.HistorySaveEvent;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface HistoryService {
    History createNewHistory(HistorySaveEvent historySaveEvent);
    List<History> getAllHistory(Long ticketId) throws EntityNotFoundException;
}
