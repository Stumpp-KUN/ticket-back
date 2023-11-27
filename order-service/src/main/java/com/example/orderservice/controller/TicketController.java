package com.example.orderservice.controller;

import com.example.orderservice.dto.TicketDTO;
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
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @GetMapping("/collect")
    public ResponseEntity<List<Ticket>> collect(@AuthenticationPrincipal Jwt jwt, @RequestParam Integer type, @RequestParam Boolean available) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTickets(jwt, type, available));
    }

    @PostMapping("/create")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO, @RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.createTicket(ticketDTO, userEmail));
    }

    @PutMapping("/update")
    public ResponseEntity<TicketDTO> updateTicket(@RequestBody @Valid TicketDTO ticketDTO, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateTicket(ticketDTO,userEmail));
    }

    @PutMapping("/update/state")
    public ResponseEntity<Ticket> updateTicketState(@RequestBody Ticket ticket, @RequestParam String state, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateTicketState(ticket,state,userEmail));
    }

    @PutMapping("/update/assign")
    public ResponseEntity<Ticket> updateTicketAssigner(@RequestBody Ticket ticket, @RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.updateAssigner(ticket,userEmail));
    }

    @GetMapping("/review/manager")
    public ResponseEntity<List<Ticket>> findAllTicketForManagerReview(@RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicketManagerReview(userEmail));
    }

    @GetMapping("/review/employee")
    public ResponseEntity<List<Ticket>> findAllTicketForEmployeeChange(@RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicketEmployeeReview(userEmail));
    }

    @GetMapping("/review/engineer")
    public ResponseEntity<List<Ticket>> findAllTicketForEngineerReview() {
        return ResponseEntity.ok(ticketService.getTicketEngineerReview());
    }

}
