package com.example.orderservice.service;

import com.example.orderservice.dto.TicketCreateDTO;
import com.example.orderservice.dto.TicketReadDTO;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.exception.KafkaException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TicketService {
    TicketReadDTO getById(Long id) throws EntityNotFoundException;
    List<TicketReadDTO> getTicketManagerReview(String userEmail) throws EntityNotFoundException;
    List<TicketReadDTO> getTicketEmployeeReview(String userEmail) throws EntityNotFoundException;
    List<TicketReadDTO> getTicketEngineerReview();
    TicketReadDTO updateAssigner(Ticket ticket, String userEmail) throws EntityNotFoundException, KafkaException;
    List<TicketReadDTO> getTickets(Jwt token, Integer type, Boolean available) throws EntityNotFoundException;
    TicketReadDTO createTicket(TicketCreateDTO ticketCreateDTO, String userEmail) throws EntityNotFoundException;
    TicketReadDTO updateTicket(TicketReadDTO ticketReadDTO, String userEmail) throws EntityNotFoundException, KafkaException;
    TicketReadDTO updateTicketState(Ticket ticket, String state, String userEmail) throws EntityNotFoundException, KafkaException;
}
