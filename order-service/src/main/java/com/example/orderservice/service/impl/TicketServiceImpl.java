package com.example.orderservice.service.impl;

import com.example.orderservice.dto.TicketDTO;
import com.example.orderservice.entity.*;
import com.example.orderservice.event.HistorySaveEvent;
import com.example.orderservice.event.MailEvent;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.exception.KafkaException;
import com.example.orderservice.repository.TicketsRepository;
import com.example.orderservice.service.TicketService;
import com.example.orderservice.util.Sorter;
import com.example.orderservice.util.TicketUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketsRepository ticketsRepository;

    private final UserServiceImpl userServiceImpl;

    private final Sorter sorter;
    private final TicketUtil ticketUtil;

    private final KafkaTemplate<String, HistorySaveEvent> kafkaTemplate;
    private final KafkaTemplate<String, MailEvent> kafkaTemplateMail;

    @Override
    public Ticket getTicket(Long id) throws EntityNotFoundException {
        log.info("Getting ticket with id {}", id);

        return ticketsRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("There is not ticket with id " + id));
    }

    @Override
    public List<Ticket> getTicketManagerReview(String userEmail) throws EntityNotFoundException {
        log.info("Getting tickets for manager review");

        User user = userServiceImpl.getByUserEmail(userEmail);

        List<Ticket> viewEmployeeTickets = ticketsRepository.findAllOwnerEmployeeAndStateNew();
        List<Ticket> declinedManagerTickets = ticketsRepository.findAllByOwnerIdAndStateId_Declined(user);
        List<Ticket> allViewTickets = ticketsRepository.findAllByOwnerIdAndStateId_Draft(user);

        allViewTickets.addAll(viewEmployeeTickets);
        allViewTickets.addAll(declinedManagerTickets);
        return allViewTickets;
    }

    @Override
    public List<Ticket> getTicketEmployeeReview(String userEmail) throws EntityNotFoundException {
        log.info("Getting tickets for employee changing");

        User user = userServiceImpl.getByUserEmail(userEmail);

        return ticketsRepository.findAllByOwnerIdAndStateId_DraftAndStateId_Declined(user);
    }

    @Override
    public List<Ticket> getTicketEngineerReview() {
        log.info("Getting tickets for engineer review");

        return ticketsRepository.findAllApprovedAndInProgressTickets();
    }

    @Transactional
    @Override
    public Ticket updateAssigner(Ticket ticket, String userEmail) throws EntityNotFoundException, KafkaException {
        log.info("Updating assigner, to ticket {}", ticket);

        User user = userServiceImpl.getByUserEmail(userEmail);

        ticket.setAssigneer(user);

        try {
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(ticket.getId())
                    .userId(user.getId())
                    .action(Action.ASSIGNER_ADDED)
                    .description("Added new assigner " + user.getFirstName() + " " + user.getLastName())
                    .build());

            kafkaTemplateMail.send("mailTopic", MailEvent.builder()
                    .mail(ticket.getOwnerId().getEmail())
                    .subject("New assigner!")
                    .message("Hello, " + ticket.getOwnerId().getFirstName() +
                            ". Your ticket assigner is updated " + ticket.getId() + ticket.getName() +
                            " now, your ticket assigner is " + user.getFirstName() + " " + user.getLastName()+
                            " . Regards, ticket-ordering")
                    .build());

        } catch (Exception e) {
            throw new KafkaException("Error kafka mail sending");
        }

        return ticketsRepository.save(ticket);
    }

    @Override
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
    @Override
    public TicketDTO createTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException {
        log.info("Creating ticket {}", ticketDTO);

        Ticket ticket = ticketUtil.createTicket(ticketDTO, userEmail);
        log.info(ticket.toString());

        try {
            Ticket savingTicket = ticketsRepository.save(ticket);
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(savingTicket.getId())
                    .userId(userServiceImpl.getByUserEmail(userEmail).getId())
                    .action(Action.TICKET_IS_CREATED)
                    .description("Created ticket")
                    .build());
        } catch (Exception e) {
            log.error("Error sending Kafka message", e);
            throw new RuntimeException("Error creating ticket", e);
        }

        return null;
    }

    @Transactional
    @Override
    public TicketDTO updateTicket(TicketDTO ticketDTO, String userEmail) throws EntityNotFoundException, KafkaException {
        log.info("Updating ticket {}", ticketDTO);

        User user = userServiceImpl.getByUserEmail(userEmail);
        Ticket ticket = ticketUtil.createTicket(ticketDTO, userEmail);
        ticketsRepository.save(ticket);

        try {
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(ticket.getId())
                    .userId(user.getId())
                    .action(Action.TICKET_IS_EDITED)
                    .description("Ticket was EDITED " + ticket.getName())
                    .build());

            kafkaTemplateMail.send("mailTopic", MailEvent.builder()
                    .mail(ticket.getOwnerId().getEmail())
                    .message("Hello, " + ticket.getOwnerId().getFirstName() +
                            ". Your ticket status " + ticket.getId() + ticket.getName() +
                            " was changed, to " + ticket.getStateId() +
                            " status. Regards, ticket-ordering")
                    .subject("Ticket status is changed")
                    .build());

        } catch (Exception e) {
            throw new KafkaException("Error kafka mail sending");
        }

        return null;
    }

    @Transactional
    @Override
    public Ticket updateTicketState(Ticket ticket, String state, String userEmail) throws EntityNotFoundException, KafkaException {
        log.info("Changing state ticket {}, to state {}", ticket, state);

        State state1 = State.valueOf(state);
        log.info(state1.name());
        ticket.setStateId(state1);

        User user = userServiceImpl.getByUserEmail(userEmail);


        if (state1 == State.APPROVED) {
            log.info("Added new approver");
            ticket.setApprover(user);
        }

        Ticket savingTicket = ticketsRepository.save(ticket);

        try {
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(savingTicket.getId())
                    .userId(user.getId())
                    .action(Action.TICKET_STATUS_IS_CHANGED)
                    .description("Changed ticket status info to " + state1.name())
                    .build());

            kafkaTemplateMail.send("mailTopic", MailEvent.builder()
                    .mail(savingTicket.getOwnerId().getEmail())
                    .subject("ATTENTION!")
                    .message("Hello, " + savingTicket.getOwnerId().getFirstName() +
                            ". Your ticket status " + ticket.getId() + ticket.getName() +
                            " was changed, to " + savingTicket.getStateId() +
                            " status. Regards, ticket-ordering")
                    .subject("Ticket status is changed")
                    .build());

        } catch (Exception e) {
            throw new KafkaException("Error kafka mail sending");
        }

        log.info("Added new history log");

        return ticket;
    }

}
