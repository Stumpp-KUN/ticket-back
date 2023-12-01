package com.example.orderservice.util;

import com.example.orderservice.dto.TicketCreateDTO;
import com.example.orderservice.entity.Attachment;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.entity.User;
import com.example.orderservice.enums.Role;
import com.example.orderservice.enums.State;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.mapper.TicketMapper;
import com.example.orderservice.mapper.UserMapper;
import com.example.orderservice.repository.TicketsRepository;
import com.example.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketUtil {
    private final TicketsRepository ticketsRepository;
    private final UserService userService;
    private final TicketMapper ticketMapper;
    private final UserMapper userMapper;

    public List<Ticket> findTicketByRole(Jwt token) throws EntityNotFoundException {
        if (token.getClaim("realm_access") != null) {
            Map<String, Object> realmAccess = token.getClaim("realm_access");
            String userEmail = token.getClaim("email");
            if (realmAccess.containsKey("roles")) {
                List<String> realmRoles = (List<String>) realmAccess.get("roles");
                if (realmRoles.contains("EMPLOYEE")) {
                return ticketsRepository.findAllByOwnerId(userMapper.toEntity(userService.getByUserEmail(userEmail)));
                } else if (realmRoles.contains("MANAGER")) {
                    User user=userMapper.toEntity(userService.getByUserEmail(userEmail));
                    log.info(user.toString());
                    List<Ticket> managerTicket = ticketsRepository.findAllByOwnerId(user);
                    List<Ticket> employeeTicketsAtNewStatus = ticketsRepository.findAllOwnerRoleAndState(Role.EMPLOYEE.name(), State.NEW.name());
                    List<Ticket> approverAndStatus = ticketsRepository.findTicketsByApproverAndStates(userEmail);

                    HashSet<Ticket> uniqueTickets= new HashSet<>();
                    uniqueTickets.addAll(managerTicket);
                    uniqueTickets.addAll(employeeTicketsAtNewStatus);
                    uniqueTickets.addAll(approverAndStatus);

                    List<Ticket> allManagerTickets = new ArrayList<>(uniqueTickets);
                    return allManagerTickets;

                } else if (realmRoles.contains("ENGINEER")) {
                    List<Ticket> managerAndEmployeeTickets = ticketsRepository.findAllByStateId(State.APPROVED.name());
                    List<Ticket> assignTicketsInProgress = ticketsRepository.findTicketsByAssigneeInStates(userEmail, State.IN_PROGRESS.name());
                    List<Ticket> assignTicketsInDone = ticketsRepository.findTicketsByAssigneeInStates(userEmail, State.DONE.name());

                    List<Ticket> allEngineerTickets = new ArrayList<>();
                    allEngineerTickets.addAll(managerAndEmployeeTickets);
                    allEngineerTickets.addAll(assignTicketsInProgress);
                    allEngineerTickets.addAll(assignTicketsInDone);

                    return allEngineerTickets;

                }

            }
        } else new EntityNotFoundException("Incorrect role");
        return null;
    }

    public List<Ticket> findOwnTickets(Jwt token) throws EntityNotFoundException {
        if (token.getClaim("realm_access") != null) {
            String userEmail = token.getClaim("email");
            return ticketsRepository.findAllByOwnerId
                    (userMapper.toEntity(userService.getByUserEmail(userEmail)));
        }
        else new EntityNotFoundException("Incorrect role");
        return null;

    }

    public Ticket mapTicket(TicketCreateDTO ticketCreateDTO, String userEmail) throws EntityNotFoundException {
        Ticket ticket = ticketMapper.toEntityFromCreate(ticketCreateDTO);
        User user = userMapper.toEntity(userService.getByUserEmail(userEmail));
        ticket.setCreatedOn(LocalDate.now());
        ticket.setStateId(State.NEW);
        ticket.setOwnerId(user);
        return ticket;
    }

    private List<Attachment> attachmentCheck(List<Attachment> attachments){
        List<String> allowedExtensions = Arrays.asList("pdf", "doc", "docx", "png", "jpeg", "jpg");

        for (Attachment attachment : attachments) {
            long fileSize = attachment.getBlob().length;
            String originalFilename = attachment.getName();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

            if (!allowedExtensions.contains(fileExtension)) {
                attachment=null;
                log.error("Attachment extensions is valid");
            }
            if (fileSize > 5 * 1024 * 1024) {
                log.error("The size of attached file should not be greater than 5 MB");
                attachment.setBlob(null);
            }
        }
        return attachments;

    }
}
