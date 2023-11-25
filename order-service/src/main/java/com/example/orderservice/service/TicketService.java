package com.example.orderservice.service;

import com.example.orderservice.dto.TicketDTO;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.exception.KafkaException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface TicketService {

    Ticket getTicket(Long id) throws EntityNotFoundException;

    List<Ticket> getTicketManagerReview(String userEmail) throws EntityNotFoundException;

    List<Ticket> getTicketEmployeeReview(String userEmail) throws EntityNotFoundException;

    List<Ticket> getTicketEngineerReview();

    Ticket updateAssigner(Ticket ticket, String userEmail) throws EntityNotFoundException, KafkaException;

    List<Ticket> getTickets(Jwt token, Integer type, Boolean available) throws EntityNotFoundException;

    TicketDTO createTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException;

    TicketDTO updateTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException, KafkaException;

    Ticket updateTicketState(Ticket ticket, String state, String userEmail) throws EntityNotFoundException, KafkaException;

}

