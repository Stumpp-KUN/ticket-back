package com.example.orderservice.controller;

import com.example.orderservice.dto.TicketDTO;
import com.example.orderservice.entity.State;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.service.TicketService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ticket")
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicket(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }

    @GetMapping("/collect")
    public ResponseEntity<List<Ticket>> collect(@AuthenticationPrincipal Jwt jwt) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getTickets(jwt));
    }

    @GetMapping("/collect/own")
    public ResponseEntity<List<Ticket>> collectOwnTickets(@AuthenticationPrincipal Jwt jwt) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.getOwnTickets(jwt));
    }

    @GetMapping("/collect/sort/id")
    public ResponseEntity<List<Ticket>> sortById(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.sortById(jwt));
    }

    @GetMapping("/collect/sort/name")
    public ResponseEntity<List<Ticket>> sortByName(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.sortByName(jwt));
    }

    @GetMapping("/collect/sort/name/inversed")
    public ResponseEntity<List<Ticket>> sortByNameInversed(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.sortByNameInverse(jwt));
    }

    @GetMapping("/collect/sort/desired/asc")
    public ResponseEntity<List<Ticket>> sortByDesiredASC(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(ticketService.sortByDesiredASC(jwt));
    }

    @GetMapping("/collect/sort/desired/des")
    public ResponseEntity<List<Ticket>> sortByDesiredDES(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(ticketService.sortByDesiredDES(jwt));
    }

    @GetMapping("/collect/sort/urgency/high")
    public ResponseEntity<List<Ticket>> sortByUrgencyHigh(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(ticketService.sortByUrgencyHigh(jwt));
    }

    @GetMapping("/collect/sort/urgency/low")
    public ResponseEntity<List<Ticket>> sortByUrgencyLow(@AuthenticationPrincipal Jwt jwt){
        return ResponseEntity.ok(ticketService.sortByUrgencyLow(jwt));
    }

    @PostMapping("/create")
    public ResponseEntity<Ticket> createTicket(@RequestBody @Valid TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException {
        return ResponseEntity.ok(ticketService.createTicket(ticketDTO,userEmail));
    }

    @PutMapping("/update")
    public ResponseEntity<Ticket> updateTicket(@RequestBody @Valid TicketDTO ticketDTO) {


    }

}
