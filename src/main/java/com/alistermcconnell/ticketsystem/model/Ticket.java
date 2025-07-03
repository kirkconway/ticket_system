package com.alistermcconnell.ticketsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

    @Id
    private String ticketId;
    private String subject;
    private String description;
    private Status status;
    private String userId;
    private String assigneeId;
    private Date createdAt;
    private Date updatedAt;

}
