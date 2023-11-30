package com.example.orderservice.entity;

import com.example.orderservice.enums.State;
import com.example.orderservice.enums.Urgency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "tickets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket implements Comparable<Ticket> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "created_on")
    private LocalDate createdOn;

    @Column(name = "desired_resolution_date")
    private LocalDate desiredResolutionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state_id")
    private State stateId;

    @ManyToOne
    @JoinColumn(name = "assigneer")
    private User assigneer;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_id")
    private Urgency urgencyId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category_id;

    @ManyToOne
    @JoinColumn(name = "approver")
    private User approver;

    @Override
    public int compareTo(Ticket o) {
        return this.desiredResolutionDate.compareTo(o.desiredResolutionDate);
    }

}
