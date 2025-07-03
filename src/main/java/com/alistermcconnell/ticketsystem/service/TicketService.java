package com.alistermcconnell.ticketsystem.service;

import com.alistermcconnell.ticketsystem.dto.AssignRequest;
import com.alistermcconnell.ticketsystem.dto.TicketRequest;
import com.alistermcconnell.ticketsystem.dto.UpdateStatusRequest;
import com.alistermcconnell.ticketsystem.model.Status;
import com.alistermcconnell.ticketsystem.model.Ticket;
import com.alistermcconnell.ticketsystem.repository.TicketRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TicketService {


    private final RedissonClient redisson;
    private final TicketRepository ticketRepository;

    public TicketService(RedissonClient redisson, TicketRepository ticketRepository) {
        this.redisson = redisson;
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(TicketRequest ticketRequest) {
        return ticketRepository.save(new Ticket(UUID.randomUUID().toString(), ticketRequest.getSubject(), ticketRequest.getDescription(), Status.Open, ticketRequest.getUserId(), null, new Date(), new Date()));
    }

    public Ticket updateStatus(String uuid, UpdateStatusRequest updateStatusRequest) {
        return executeWithLock("ticket:" + uuid,
                () -> {
                    Ticket ticket = ticketRepository.findById(uuid).orElseThrow();
                    ticket.setStatus(updateStatusRequest.getStatus());
                    return ticketRepository.save(ticket);
                });
    }

    public Ticket assign(String uuid, AssignRequest assignRequest) {
        return executeWithLock("ticket:" + uuid,
                () -> {
                    Ticket ticket = ticketRepository.findById(uuid).orElseThrow();
                    ticket.setAssigneeId(assignRequest.getAssigneeId());
                    return ticketRepository.save(ticket);
                });
    }

    public Ticket get(String uuid) {
        return ticketRepository.findById(uuid).orElseThrow();
    }

    private Ticket executeWithLock(String ticketId, TicketUpdater updater) {
        String lockKey = "ticket-lock:" + ticketId;
        RLock lock = redisson.getLock(lockKey);
        try {
            if (lock.tryLock(0, 10, TimeUnit.SECONDS)) {
                return updater.update();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {

            System.out.println("Releasing lock for ticket: " + ticketId);
            if (lock.isHeldByCurrentThread()) lock.unlock();
        }
        throw new RuntimeException("Lock not acquired for ticket: " + ticketId);
    }

    @FunctionalInterface
    private interface TicketUpdater {
        Ticket update();
    }
}
