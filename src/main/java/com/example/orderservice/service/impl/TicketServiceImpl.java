package com.example.orderservice.service.impl;

import com.example.orderservice.dto.TicketCreateDTO;
import com.example.orderservice.dto.TicketReadDTO;
import com.example.orderservice.dto.UserDTO;
import com.example.orderservice.entity.*;
import com.example.orderservice.enums.Action;
import com.example.orderservice.enums.Role;
import com.example.orderservice.enums.State;
import com.example.orderservice.event.HistorySaveEvent;
import com.example.orderservice.event.MailEvent;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.exception.KafkaException;
import com.example.orderservice.mapper.TicketMapper;
import com.example.orderservice.mapper.UserMapper;
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
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketsRepository ticketsRepository;

    private final UserServiceImpl userService;

    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;

    private final Sorter sorter;
    private final TicketUtil ticketUtil;

    private final KafkaTemplate<String, HistorySaveEvent> kafkaTemplate;
    private final KafkaTemplate<String, MailEvent> kafkaTemplateMail;

    @Override
    public TicketReadDTO getById(Long id) throws EntityNotFoundException {
        log.info("Getting ticket with id {}", id);

        return ticketMapper.fromEntity(ticketsRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("There is not ticket with id " + id)));
    }

    @Override
    public List<TicketReadDTO> getTicketManagerReview(String userEmail) throws EntityNotFoundException {
        log.info("Getting tickets for manager review");

        User user = parseUserDtoToUser(userService.getByUserEmail(userEmail));

        List<Ticket> viewEmployeeTickets = ticketsRepository.findAllOwnerRoleAndState(Role.EMPLOYEE.name(), State.NEW.name());
        List<Ticket> declinedManagerTickets = ticketsRepository.findAllByOwnerIdAndStateId(user.getEmail(), State.DECLINED.name());
        List<Ticket> allViewTickets = ticketsRepository.findAllByOwnerIdAndStateId(user.getEmail(), State.DRAFT.name());

        allViewTickets.addAll(viewEmployeeTickets);
        allViewTickets.addAll(declinedManagerTickets);
        System.out.println(allViewTickets.size());
        return parseFromEntityList(allViewTickets);
    }

    @Override
    public List<TicketReadDTO> getTicketEmployeeReview(String userEmail) throws EntityNotFoundException {
        log.info("Getting tickets for employee changing");

        User user = parseUserDtoToUser(userService.getByUserEmail(userEmail));

        List<Ticket> ticketsDeclined = ticketsRepository.findAllByOwnerIdAndStateId(user.getEmail(), State.DECLINED.name());
        List<Ticket> ticketsDraft = ticketsRepository.findAllByOwnerIdAndStateId(user.getEmail(), State.DRAFT.name());

        ticketsDeclined.addAll(ticketsDraft);

        return parseFromEntityList(ticketsDeclined);
    }

    @Override
    public List<TicketReadDTO> getTicketEngineerReview() {
        log.info("Getting tickets for engineer review");

        List<Ticket> approvedTickets = ticketsRepository.findAllByStateId(State.APPROVED.name());
        List<Ticket> inProgressTickets = ticketsRepository.findAllByStateId(State.IN_PROGRESS.name());

        approvedTickets.addAll(inProgressTickets);

        return parseFromEntityList(approvedTickets);
    }

    @Transactional
    @Override
    public TicketReadDTO updateAssigner(Ticket ticket, String userEmail) throws EntityNotFoundException, KafkaException {
        log.info("Updating assigner, to ticket {}", ticket);

        User user = parseUserDtoToUser(userService.getByUserEmail(userEmail));

        ticket.setAssigneer(user);

        try {
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(ticket.getId())
                    .userId(user.getId())
                    .action(Action.ASSIGNER_ADDED)
                    .description("Assigner was changed to " + user.getFirstName() + " " + user.getLastName())
                    .build());

            kafkaTemplateMail.send("mailTopic", MailEvent.builder()
                    .mail(ticket.getOwnerId().getEmail())
                    .subject("Assigner was updated!")
                    .message("Hello, " + ticket.getOwnerId().getFirstName() +
                            ". Your ticket assigner was update " + ticket.getAssigneer().getFirstName() + ticket.getAssigneer().getLastName() +
                            " was changed, to " + ticket.getStateId() +
                            " . Regards, ticket-ordering")
                    .subject("Ticket assigner is updated")
                    .build());

        } catch (Exception e) {
            throw new KafkaException("Error kafka mail sending");
        }

        return ticketMapper.fromEntity(ticketsRepository.save(ticket));
    }

    @Override
    public List<TicketReadDTO> getTickets(Jwt token, Integer type, Boolean available) throws EntityNotFoundException {
        switch (type) {
            case 1: {
                return parseFromEntityList(sorter.getAllAvailable(token));
            }
            case 2: {
                return parseFromEntityList(sorter.getOwnTickets(token));
            }
            case 3: {
                return parseFromEntityList(sorter.sortById(token, available));
            }
            case 4: {
                return parseFromEntityList(sorter.sortByName(token, available));
            }
            case 5: {
                return parseFromEntityList(sorter.sortByNameInverse(token, available));
            }
            case 6: {
                return parseFromEntityList(sorter.sortByDesiredDES(token, available));
            }
            case 7: {
                return parseFromEntityList(sorter.sortByDesiredASC(token, available));
            }
            case 8: {
                return parseFromEntityList(sorter.sortByUrgencyHig(token, available));
            }
            case 9: {
                return parseFromEntityList(sorter.sortByUrgencyLow(token, available));
            }
            default: {
                log.error("Error type of sorting");

                return null;
            }
        }
    }

    @Transactional
    @Override
    public TicketReadDTO createTicket(TicketCreateDTO ticketCreateDTO, String userEmail) throws EntityNotFoundException {
        log.info("Creating ticket {}", ticketCreateDTO);

        Ticket ticket = ticketUtil.createTicket(ticketCreateDTO, userEmail);
        log.info(ticket.toString());

        try {
            Ticket savingTicket = ticketsRepository.save(ticket);
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(savingTicket.getId())
                    .userId(userService.getByUserEmail(userEmail).getId())
                    .action(Action.TICKET_IS_CREATED)
                    .description("Created ticket")
                    .build());

        } catch (Exception e) {
            log.error("Error sending Kafka message", e);
            throw new RuntimeException("Error creating ticket", e);
        }

        return ticketMapper.fromEntity(ticket);
    }

    @Transactional
    @Override
    public TicketReadDTO updateTicket(TicketReadDTO ticketReadDTO, String userEmail) throws EntityNotFoundException, KafkaException {
        log.info("Updating ticket {}", ticketReadDTO);

        User user = parseUserDtoToUser(userService.getByUserEmail(userEmail));
        Ticket ticket = ticketMapper.toEntity(ticketReadDTO);

        try {
            kafkaTemplate.send("historyTopic", HistorySaveEvent.builder()
                    .ticketId(ticket.getId())
                    .userId(user.getId())
                    .action(Action.TICKET_IS_EDITED)
                    .description("Updated ticket")
                    .build());

            kafkaTemplateMail.send("mailTopic", MailEvent.builder()
                    .mail(ticket.getOwnerId().getEmail())
                    .subject("ATTENTION!")
                    .message("Hello, " + ticket.getOwnerId().getFirstName() +
                            ". Your ticket " + ticket.getId() + ticket.getName() +
                            " was updated")
                    .subject("Ticket is updated")
                    .build());

        } catch (Exception e) {
            throw new KafkaException("Error kafka mail sending");
        }

        return null;
    }

    @Transactional
    @Override
    public TicketReadDTO updateTicketState(Ticket ticket, String state, String userEmail) throws EntityNotFoundException, KafkaException {
        log.info("Changing state ticket {}, to state {}", ticket, state);

        State state1 = State.valueOf(state);
        log.info(state1.name());
        ticket.setStateId(state1);

        User user = parseUserDtoToUser(userService.getByUserEmail(userEmail));


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
                    .message("Hello, " + user.getFirstName() +
                            ". Your ticket status " + ticket.getId() + ticket.getName() +
                            " was changed, to " + savingTicket.getStateId() +
                            " status. Regards, ticket-ordering")
                    .subject("Ticket status is changed")
                    .build());

        } catch (Exception e) {
            throw new KafkaException("Error kafka mail sending");
        }

        log.info("Added new history log");

        return ticketMapper.fromEntity(ticket);
    }

    private List<TicketReadDTO> parseFromEntityList(List<Ticket> tickets) {
        return tickets.stream()
                .map(ticketMapper::fromEntity)
                .collect(Collectors.toList());
    }

    private User parseUserDtoToUser(UserDTO userDTO) {
        return userMapper.toEntity(userDTO);
    }

}
