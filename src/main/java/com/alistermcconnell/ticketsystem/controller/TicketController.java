package com.alistermcconnell.ticketsystem.controller;

import com.alistermcconnell.ticketsystem.dto.AssignRequest;
import com.alistermcconnell.ticketsystem.dto.TicketRequest;
import com.alistermcconnell.ticketsystem.dto.UpdateStatusRequest;
import com.alistermcconnell.ticketsystem.model.Ticket;
import com.alistermcconnell.ticketsystem.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }


    @GetMapping("/{uuid}")
    public ResponseEntity<Ticket> get(@PathVariable String uuid) {
        return ResponseEntity.ok(service.get(uuid));
    }

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(service.createTicket(ticketRequest));
    }

    @PatchMapping("/{uuid}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable String uuid, @RequestBody UpdateStatusRequest updateStatusRequest) {

        try {
            return ResponseEntity.ok(service.updateStatus(uuid, updateStatusRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PatchMapping("/{uuid}/assign")
    public ResponseEntity<Ticket> assign(@PathVariable String uuid, @RequestBody AssignRequest assignRequest) {
        try {
            return ResponseEntity.ok(service.assign(uuid, assignRequest));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

}
