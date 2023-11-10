package com.example.orderservice.util;

import com.example.orderservice.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Sorter {

    private final TicketUtil ticketUtil;

    public List<Ticket> sortById(Jwt token){
        List<Ticket> allUserTickets = ticketUtil.findTicketByRole(token);
        Collections.sort(allUserTickets, Comparator.comparing(Ticket::getId));
        return allUserTickets;
    }

    public List<Ticket> sortByName(Jwt token){
        List<Ticket> allUsersTickets = ticketUtil.findTicketByRole(token);
        Collections.sort(allUsersTickets, Comparator.comparing(Ticket::getName));
        return allUsersTickets;
    }

    public List<Ticket> sortByNameInverse(Jwt token){
        List<Ticket> allUsersTickets = ticketUtil.findTicketByRole(token);
        Collections.sort(allUsersTickets, Comparator.comparing(Ticket::getName).reversed());
        return allUsersTickets;
    }

    public List<Ticket> sortByDesiredASC(Jwt token){
        List<Ticket> allUsersTickets = ticketUtil.findTicketByRole(token);
        Collections.sort(allUsersTickets, Comparator.comparing(Ticket::getDesiredResolutionDate));
        return allUsersTickets;
    }




}
