package com.example.orderservice.controller;

import com.example.orderservice.dto.TicketCreateDTO;
import com.example.orderservice.dto.TicketReadDTO;
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
    public ResponseEntity<TicketReadDTO> getTicket(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @GetMapping("/collect")
    public ResponseEntity<List<TicketReadDTO>> collect(@AuthenticationPrincipal Jwt jwt, @RequestParam Integer type) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTickets(jwt, type));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createTicket(@RequestBody TicketCreateDTO ticketCreateDTO, @RequestParam String userEmail) throws EntityNotFoundException {
        ticketService.createTicket(ticketCreateDTO, userEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateTicket(@RequestBody @Valid TicketReadDTO ticketReadDTO, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        ticketService.updateTicket(ticketReadDTO,userEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/state")
    public ResponseEntity<Void> updateTicketState(@RequestBody Ticket ticket, @RequestParam String state, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        ticketService.updateTicketState(ticket,state,userEmail);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/assign")
    public ResponseEntity<Void> updateTicketAssigner(@RequestBody Ticket ticket, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        ticketService.updateAssigner(ticket,userEmail);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/review/manager")
    public ResponseEntity<List<TicketReadDTO>> findAllTicketForManagerReview(@RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicketManagerReview(userEmail));
    }

    @GetMapping("/review/employee")
    public ResponseEntity<List<TicketReadDTO>> findAllTicketForEmployeeChange(@RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicketEmployeeReview(userEmail));
    }

    @GetMapping("/review/engineer")
    public ResponseEntity<List<TicketReadDTO>> findAllTicketForEngineerReview() {
        return ResponseEntity.ok(ticketService.getTicketEngineerReview());
    }

}
