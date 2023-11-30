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
    public ResponseEntity<TicketReadDTO> createTicket(@RequestBody TicketCreateDTO ticketCreateDTO, @RequestParam String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.createTicket(ticketCreateDTO, userEmail));
    }

    @PutMapping("/update")
    public ResponseEntity<TicketReadDTO> updateTicket(@RequestBody @Valid TicketReadDTO ticketReadDTO, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateTicket(ticketReadDTO,userEmail));
    }

    @PutMapping("/update/state")
    public ResponseEntity<TicketReadDTO> updateTicketState(@RequestBody Ticket ticket, @RequestParam String state, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateTicketState(ticket,state,userEmail));
    }

    @PutMapping("/update/assign")
    public ResponseEntity<TicketReadDTO> updateTicketAssigner(@RequestBody Ticket ticket, @RequestParam String userEmail) throws EntityNotFoundException, KafkaException {
        return ResponseEntity.ok(ticketService.updateAssigner(ticket,userEmail));
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
