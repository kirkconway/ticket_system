package com.alistermcconnell.ticketsystem.controller;

import com.alistermcconnell.ticketsystem.dto.AssignRequest;
import com.alistermcconnell.ticketsystem.dto.TicketRequest;
import com.alistermcconnell.ticketsystem.dto.UpdateStatusRequest;
import com.alistermcconnell.ticketsystem.model.Ticket;
import com.alistermcconnell.ticketsystem.service.TicketService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

class TicketControllerTest {


    private final TicketService service= Mockito.mock(TicketService.class);
    private TicketController controller = new TicketController(service);

    @Test
    void testGet() {
        Ticket ticket = new Ticket();
        when(service.get("uuid")).thenReturn(ticket);

        ResponseEntity<Ticket> response = controller.get("uuid");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testCreate() {
        TicketRequest request = new TicketRequest();
        Ticket ticket = new Ticket();
        when(service.createTicket(request)).thenReturn(ticket);

        ResponseEntity<Ticket> response = controller.create(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testUpdateStatus_Success() {
        UpdateStatusRequest req = new UpdateStatusRequest();
        Ticket ticket = new Ticket();
        when(service.updateStatus("uuid", req)).thenReturn(ticket);

        ResponseEntity<Ticket> response = controller.updateStatus("uuid", req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testUpdateStatus_Exception() {
        UpdateStatusRequest req = new UpdateStatusRequest();
        when(service.updateStatus("uuid", req)).thenThrow(new RuntimeException());

        ResponseEntity<Ticket> response = controller.updateStatus("uuid", req);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testAssign_Success() {
        AssignRequest req = new AssignRequest();
        Ticket ticket = new Ticket();
        when(service.assign("uuid", req)).thenReturn(ticket);

        ResponseEntity<Ticket> response = controller.assign("uuid", req);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(ticket, response.getBody());
    }

    @Test
    void testAssign_Exception() {
        AssignRequest req = new AssignRequest();
        when(service.assign("uuid", req)).thenThrow(new RuntimeException());

        ResponseEntity<Ticket> response = controller.assign("uuid", req);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}