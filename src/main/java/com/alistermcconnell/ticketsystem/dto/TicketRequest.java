package com.alistermcconnell.ticketsystem.dto;

import lombok.Data;

@Data
public class TicketRequest {
    String userId;
    String subject;
    String description;
}
