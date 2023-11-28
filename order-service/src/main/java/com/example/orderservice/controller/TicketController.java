package com.example.orderservice.controller;

import com.example.orderservice.dto.TicketDTOCreate;
import com.example.orderservice.dto.TicketDTORead;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.exception.KafkaException;
import com.example.orderservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ticket")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTORead> getTicket(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping("/collect")
    public ResponseEntity<List<TicketDTORead>> collect(@AuthenticationPrincipal Jwt jwt, @RequestParam Integer type, @RequestParam Boolean available) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTickets(jwt, type, available));
    }

    @PostMapping("/create")
    public ResponseEntity<TicketDTORead> createTicket(@RequestBody TicketDTOCreate ticketDTOCreate, @RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.createTicket(ticketDTOCreate, userEmail));
    }

    @PutMapping("/update")
    public ResponseEntity<TicketDTORead> updateTicket(@RequestBody @Valid TicketDTORead ticketDTORead, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateTicket(ticketDTORead,userEmail));
    }

    @PutMapping("/update/state")
    public ResponseEntity<TicketDTORead> updateTicketState(@RequestBody Ticket ticket, @RequestParam String state, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateTicketState(ticket,state,userEmail));
    }

    @PutMapping("/update/assign")
    public ResponseEntity<TicketDTORead> updateTicketAssigner(@RequestBody Ticket ticket, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateAssigner(ticket,userEmail));
    }

    @GetMapping("/review/manager")
    public ResponseEntity<List<TicketDTORead>> findAllTicketForManagerReview(@RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicketManagerReview(userEmail));
    }

    @GetMapping("/review/employee")
    public ResponseEntity<List<TicketDTORead>> findAllTicketForEmployeeChange(@RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicketEmployeeReview(userEmail));
    }

    @GetMapping("/review/engineer")
    public ResponseEntity<List<TicketDTORead>> findAllTicketForEngineerReview() {
        return ResponseEntity.ok(ticketService.getTicketEngineerReview());
    }

}
