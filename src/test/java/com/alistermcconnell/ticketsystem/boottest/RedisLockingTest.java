package com.alistermcconnell.ticketsystem.boottest;

import com.alistermcconnell.ticketsystem.dto.AssignRequest;
import com.alistermcconnell.ticketsystem.model.Ticket;
import com.alistermcconnell.ticketsystem.repository.TicketRepository;
import com.alistermcconnell.ticketsystem.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisLockingTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    private String ticketId;

    @BeforeEach
    void setup() {
        ticketRepository.deleteAll();
        Ticket ticket = new Ticket();
        ticket.setTicketId("test-ticket-id");
        ticket.setSubject("Redis Lock Test");
        ticket = ticketRepository.save(ticket);
        this.ticketId = ticket.getTicketId();
    }

    @Test
    void testRedisLockPreventsConcurrentUpdates() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Future<Boolean> future = executor.submit(() -> {
                System.out.println("waiting to start!");
                latch.countDown();
                latch.await();
                try {
                    // This method should use Redisson lock internally
                    Ticket updated = ticketService.assign(ticketId, new AssignRequest("agent-" + index));
                    //System.out.println("empy?" + updated);
                    return updated != null && ("agent-" + index).equals(updated.getAssigneeId());
                } catch (Exception e) {
                    return false;
                }
            });
            futures.add(future);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        long successCount = futures.stream().filter(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                return false;
            }
        }).count();

        assertThat(successCount).isEqualTo(1);
    }
}