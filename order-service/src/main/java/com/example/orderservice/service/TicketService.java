package com.example.orderservice.service;

import com.example.orderservice.dto.TicketDTO;
import com.example.orderservice.entity.*;
import com.example.orderservice.event.HistorySaveEvent;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.mapper.TicketMapper;
import com.example.orderservice.repository.CategoryRepository;
import com.example.orderservice.repository.HistoryRepository;
import com.example.orderservice.repository.TicketsRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.util.Sorter;
import com.example.orderservice.util.TicketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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

    private final Sorter sorter;
    private final TicketUtil ticketUtil;

    private final KafkaTemplate kafkaTemplate;

    public Ticket getTicket(Long id) throws EntityNotFoundException {
        log.info("Getting ticket with id {}", id);

        return ticketsRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("There is not ticket with id " + id));
    }

    public List<Ticket> getTicketManagerReview(String userEmail) throws EntityNotFoundException {
        log.info("Getting tickets for manager review");

        User user = userService.getByUserEmail(userEmail);

        List<Ticket> viewEmployeeTickets = ticketsRepository.findAllOwnerEmployeeAndStateNew();
        List<Ticket> declinedManagerTickets = ticketsRepository.findAllByOwnerIdAndStateId_Declined(user);
        List<Ticket> allViewTickets = ticketsRepository.findAllByOwnerIdAndStateId_Draft(user);

        allViewTickets.addAll(viewEmployeeTickets);
        allViewTickets.addAll(declinedManagerTickets);
        return allViewTickets;
    }

    public List<Ticket> getTicketEmployeeReview(String userEmail) throws EntityNotFoundException {
        log.info("Getting tickets for employee changing");

        User user = userService.getByUserEmail(userEmail);

        return ticketsRepository.findAllByOwnerIdAndStateId_DraftAndStateId_Declined(user);
    }

    public List<Ticket> getTicketEngineerReview() {
        log.info("Getting tickets for engineer review");

        return ticketsRepository.findAllApprovedAndInProgressTickets();
    }

    @Transactional
    public Ticket updateAssigner(Ticket ticket, String userEmail) throws EntityNotFoundException {
        log.info("Updating assigner, to ticket {}", ticket);

        User user = userService.getByUserEmail(userEmail);

        ticket.setAssigneer(user);

        historyService.createNewHistory
                (new History(ticket, Action.TICKET_IS_EDITED, user, "Added assigner "+user.getFirstName()));

        return ticketsRepository.save(ticket);
    }

    public List<Ticket> getTickets(Jwt token, Integer type, Boolean available) throws EntityNotFoundException {
        switch (type) {
            case 1: {
                return sorter.getAllAvailable(token);
            }
            case 2: {
                return sorter.getOwnTickets(token);
            }
            case 3: {
                return sorter.sortById(token, available);
            }
            case 4: {
                return sorter.sortByName(token, available);
            }
            case 5: {
                return sorter.sortByNameInverse(token, available);
            }
            case 6: {
                return sorter.sortByDesiredDES(token, available);
            }
            case 7: {
                return sorter.sortByDesiredASC(token, available);
            }
            case 8: {
                return sorter.sortByUrgencyHig(token, available);
            }
            case 9: {
                return sorter.sortByUrgencyLow(token, available);
            }
            default: {
                log.error("Error type of sorting");

                return null;
            }
        }
    }

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException {
        log.info("Creating ticket {}", ticketDTO);

       Ticket ticket=ticketUtil.createTicket(ticketDTO,userEmail);
       log.info(ticket.toString());

        kafkaTemplate.send("historyTopic", new HistorySaveEvent(ticket, LocalDate.now(), Action.TICKET_IS_CREATED, userService.getByUserEmail(userEmail), "Creating new ticket"));
        historyService.createNewHistory
                (new History(ticket, Action.TICKET_IS_CREATED, userService.getByUserEmail(userEmail), "Creating new ticket"));
        ticketsRepository.save(ticket);
        return null;
    }

    @Transactional
    public TicketDTO updateTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException {
        log.info("Updating ticket {}", ticketDTO);

        User user = userService.getByUserEmail(userEmail);
        Ticket ticket = TicketMapper.INSTANCE.toTicket(ticketDTO);
        historyService.createNewHistory
                (new History(ticket, Action.TICKET_IS_EDITED, user, "Updating ticket"));

        return TicketMapper.INSTANCE.fromTicket(ticketsRepository.save(ticket));
    }

    @Transactional
    public Ticket updateTicketState(Ticket ticket, String state, String userEmail) throws EntityNotFoundException {
        log.info("Changing state ticket {}, to state {}", ticket, state);

        State state1 = State.valueOf(state);
        log.info(state1.name());
        ticket.setStateId(state1);

        ticketsRepository.save(ticket);

        User user = userService.getByUserEmail(userEmail);
        historyService.createNewHistory(History.builder()
                .ticket(ticket)
                .user(user)
                .date(LocalDate.now())
                .action(Action.TICKET_STATUS_IS_CHANGED)
                .description("Changed status to " + state1.name())
                .build());

        return ticket;
    }

}
