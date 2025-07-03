package com.alistermcconnell.ticketsystem.repository;

import com.alistermcconnell.ticketsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, String> {
}