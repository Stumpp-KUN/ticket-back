package com.example.orderservice.service;

import com.example.orderservice.dto.TicketDTO;
import com.example.orderservice.entity.*;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.mapper.TicketMapper;
import com.example.orderservice.repository.HistoryRepository;
import com.example.orderservice.repository.TicketsRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.util.Sorter;
import com.example.orderservice.util.TicketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketsRepository ticketsRepository;

    private final UserService userService;
    private final HistoryService historyService;

    private final TicketUtil ticketUtil;
    private final Sorter sorter;

    public Ticket getTicket(Long id) throws EntityNotFoundException {
        log.info("Getting ticket with id {}", id);

        return ticketsRepository.findById(id)
                .orElseThrow(
                        ()->new EntityNotFoundException("There is not ticket with id "+id));
    }

    public List<Ticket> getTickets(Jwt token) throws EntityNotFoundException {
        log.info("Finding all available tickets");

        return sortTickets(ticketUtil.findTicketByRole(token));
    }

    public List<Ticket> getOwnTickets(Jwt token) throws EntityNotFoundException {
        log.info("Finding all owns tickets");

        return sortTickets(ticketUtil.findOwnTickets(token));
    }

    public List<Ticket> sortById(Jwt token) {
        log.info("Sorting by ticket id");

        return sorter.sortById(token);
    }

    public List<Ticket> sortByName(Jwt token) {
        log.info("Sorting by ticket name");

        return sorter.sortByName(token);
    }

    public List<Ticket> sortByNameInverse(Jwt token) {
        log.info("Sorting by ticket name inverse");

        return sorter.sortByNameInverse(token);
    }

    public List<Ticket> sortByDesiredASC(Jwt token){
        log.info("Sorting by ticket desired date ASC");

        return sortByDesiredASC(token);
    }

    public List<Ticket> sortByDesiredDES(Jwt token){
        log.info("Sorting by ticket desired date DES");

        List<Ticket> allUsersTickets = ticketUtil.findTicketByRole(token);
        Collections.sort(allUsersTickets, Comparator.comparing(Ticket::getDesiredResolutionDate).reversed());
        return allUsersTickets;
    }

    public List<Ticket> sortByUrgencyHigh(Jwt token){
        log.info("Sorting by ticket urgency high to low");

        List<Ticket> allUsersTickets = ticketUtil.findTicketByRole(token);
        sorterByUrgencyHigh(allUsersTickets);
        return allUsersTickets;
    }

    public List<Ticket> sortByUrgencyLow(Jwt token){
        log.info("Sorting by ticket urgency low to high");

        List<Ticket> allUsersTickets = ticketUtil.findTicketByRole(token);
        sorterByUrgencyLow(allUsersTickets);
        return allUsersTickets;
    }

    private List<Ticket> sorterByUrgencyHigh(List<Ticket> tickets){
        Comparator<Ticket> urgencyComparator = Comparator.comparingInt(ticket -> {
            Urgency urgency = ticket.getUrgencyId();
            switch (urgency) {
                case CRITICAL:
                    return 0;
                case HIGH:
                    return 1;
                case MEDIUM:
                    return 2;
                case LOW:
                    return 3;
                default:
                    return 4;
            }
        });
        Collections.sort(tickets, urgencyComparator);
        return tickets;
    }
    private List<Ticket> sorterByUrgencyLow(List<Ticket> tickets){
        Comparator<Ticket> urgencyComparator = Comparator.comparingInt(ticket -> {
            Urgency urgency = ticket.getUrgencyId();
            switch (urgency) {
                case CRITICAL:
                    return 4;
                case HIGH:
                    return 3;
                case MEDIUM:
                    return 2;
                case LOW:
                    return 1;
                default:
                    return 0;
            }
        });
        Collections.sort(tickets, urgencyComparator);
        return tickets;
    }

    private List<Ticket> sortTickets(List<Ticket> tickets) {
        sorterByUrgencyHigh(tickets);

        Collections.sort(tickets, Comparator.comparing(Ticket::getDesiredResolutionDate).reversed());

        return tickets;
    }

    // CREATE

    @Transactional
    public Ticket createTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException {
        log.info("Creating ticket {}", ticketDTO);

        Ticket ticket = TicketMapper.INSTANCE.toTicket(ticketDTO);
        User user=userService.getByUserEmail(userEmail);
        ticket.setCreatedOn(LocalDate.now());
        ticket.setOwnerId(user);

        historyService.createNewHistory
                (new History(ticket,LocalDate.now(), Action.TICKET_IS_CREATED,user,"Creating new ticket"));

        return ticketsRepository.save(ticket);
    }

}
