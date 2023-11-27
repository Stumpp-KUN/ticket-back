package com.example.orderservice.util;

import com.example.orderservice.entity.Ticket;
import com.example.orderservice.enums.Urgency;
import com.example.orderservice.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Sorter {

    private final TicketUtil ticketUtil;
    private List<Ticket> allUserTickets;

    public List<Ticket> getAllAvailable(Jwt token) throws EntityNotFoundException {
        log.info("Finding all available tickets");

        return sortTickets(ticketUtil.findTicketByRole(token));
    }

    public List<Ticket> getOwnTickets(Jwt token) throws EntityNotFoundException {
        log.info("Finding all owns tickets");

        return sortTickets(ticketUtil.findOwnTickets(token));
    }

    public List<Ticket> sortById(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket id");

        if(available==true) {
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        Collections.sort(allUserTickets, Comparator.comparing(Ticket::getId));
        return allUserTickets;
    }

    public List<Ticket> sortByName(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket name");

        if(available==true) {
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        Collections.sort(allUserTickets, Comparator.comparing(Ticket::getName));
        return allUserTickets;
    }

    public List<Ticket> sortByNameInverse(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket name inverse");

        if(available==true){
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        Collections.sort(allUserTickets, Comparator.comparing(Ticket::getName).reversed());
        return allUserTickets;
    }

    public List<Ticket> sortByDesiredASC(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket desired date ASC");

        if(available==true){
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        Collections.sort(allUserTickets, Comparator.comparing(Ticket::getDesiredResolutionDate));
        return allUserTickets;
    }

    public List<Ticket> sortByDesiredDES(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket desired date DES");

        if(available==true){
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        Collections.sort(allUserTickets, Comparator.comparing(Ticket::getDesiredResolutionDate).reversed());
        return allUserTickets;
    }

    public List<Ticket> sortByUrgencyHig(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket urgency high to low");

        if(available==true){
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        sortByUrgencyHigh(allUserTickets);

        return allUserTickets;
    }

    public List<Ticket> sortByUrgencyLow(Jwt token, Boolean available) throws EntityNotFoundException {
        log.info("Sorting by ticket urgency low to high");

        if(available==true){
            allUserTickets = ticketUtil.findTicketByRole(token);
        }
        else allUserTickets = ticketUtil.findOwnTickets(token);

        sorterByUrgencyLow(allUserTickets);
        return allUserTickets;
    }

    private List<Ticket> sortByUrgencyHigh(List<Ticket> tickets) {
        Collections.sort(tickets, Comparator.comparingInt(ticket -> {
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
        }));
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
        sortByUrgencyHigh(tickets);

        Collections.sort(tickets, Comparator.comparing(Ticket::getDesiredResolutionDate).reversed());

        return tickets;
    }

}
