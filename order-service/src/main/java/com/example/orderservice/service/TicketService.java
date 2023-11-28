package com.example.orderservice.service;

import com.example.orderservice.dto.TicketDTOCreate;
import com.example.orderservice.dto.TicketDTORead;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.exception.KafkaException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TicketService {
    TicketDTORead getById(Long id) throws EntityNotFoundException;
    List<TicketDTORead> getTicketManagerReview(String userEmail) throws EntityNotFoundException;
    List<TicketDTORead> getTicketEmployeeReview(String userEmail) throws EntityNotFoundException;
    List<TicketDTORead> getTicketEngineerReview();
    TicketDTORead updateAssigner(Ticket ticket, String userEmail) throws EntityNotFoundException, KafkaException;
    List<TicketDTORead> getTickets(Jwt token, Integer type, Boolean available) throws EntityNotFoundException;
    TicketDTORead createTicket(TicketDTOCreate ticketDTOCreate, String userEmail) throws EntityNotFoundException;
    TicketDTORead updateTicket(TicketDTORead ticketDTORead, String userEmail) throws EntityNotFoundException, KafkaException;
    TicketDTORead updateTicketState(Ticket ticket, String state, String userEmail) throws EntityNotFoundException, KafkaException;
}
