package com.example.orderservice.repository;

import com.example.orderservice.entity.History;
import com.example.orderservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findAllByTicketId(Long ticketId);
}
