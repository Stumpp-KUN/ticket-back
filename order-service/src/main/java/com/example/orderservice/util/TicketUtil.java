package com.example.orderservice.util;

import com.example.orderservice.entity.Attachment;
import com.example.orderservice.entity.Ticket;
import com.example.orderservice.exception.EntityNotFoundException;
import com.example.orderservice.repository.TicketsRepository;
import com.example.orderservice.repository.UserRepository;
import com.example.orderservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TicketUtil {
    private final TicketsRepository ticketsRepository;
    private final UserService userService;

    public List<Ticket> findTicketByRole(Jwt token) throws EntityNotFoundException {
        if (token.getClaim("realm_access") != null) {
            Map<String, Object> realmAccess = token.getClaim("realm_access");
            String userEmail = token.getClaim("email");
            if (realmAccess.containsKey("roles")) {
                List<String> realmRoles = (List<String>) realmAccess.get("roles");
                if (realmRoles.contains("EMPLOYEE")) {
                    System.out.println(userEmail);
                return ticketsRepository.findAllByAssigneer(userService.getByUserEmail(userEmail));
                } else if (realmRoles.contains("MANAGER")) {
                    List<Ticket> managerTicket = ticketsRepository.findAllByAssigneer(userService.getByUserEmail(userEmail));
                    List<Ticket> employeeTicketsAtNewStatus = ticketsRepository.findAllAssigneeEmployeeAndStateNew();
                    List<Ticket> approverAndStatus = ticketsRepository.findTicketsByApproverAndStates(userEmail);

                    List<Ticket> allManagerTickets = new ArrayList<>();
                    allManagerTickets.addAll(managerTicket);
                    allManagerTickets.addAll(employeeTicketsAtNewStatus);
                    allManagerTickets.addAll(approverAndStatus);

                    return allManagerTickets;

                } else if (realmRoles.contains("ENGINEER")) {
                    List<Ticket> managerAndEmployeeTickets = ticketsRepository.findTicketsCreatedByEmployeesAndManagersInApprovedState();
                    List<Ticket> assignTicketsInProgressAndDone = ticketsRepository.findTicketsByAssigneeInInProgressAndDoneStates(userEmail);

                    List<Ticket> allEngineerTickets = new ArrayList<>();
                    allEngineerTickets.addAll(managerAndEmployeeTickets);
                    allEngineerTickets.addAll(assignTicketsInProgressAndDone);

                    return allEngineerTickets;

                }

            }
        } else new EntityNotFoundException("Incorrect role");
        return null;
    }

    public List<Ticket> findOwnTickets(Jwt token) throws EntityNotFoundException {
        if (token.getClaim("realm_access") != null) {
            String userEmail = token.getClaim("email");
            return ticketsRepository.findAllByAssigneer
                    (userService
                            .getByUserEmail(userEmail)
            );
        }
        else new EntityNotFoundException("Incorrect role");
        return null;

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
                log.error("The size of attached file should not be greater than 5 MB. Please select another file.");
                attachment.setBlob(null);
            }
        }
        return attachments;

    }
}
