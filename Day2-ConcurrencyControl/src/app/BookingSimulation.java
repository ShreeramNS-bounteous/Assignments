package app;

import config.BookingConfig;
import service.TicketInventory;
import task.UserBookingTask;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BookingSimulation {
    static void main() throws InterruptedException{

        TicketInventory inventory = new TicketInventory(BookingConfig.INITIAL_TICKETS);

        ExecutorService executor = Executors.newFixedThreadPool(BookingConfig.THREAD_POOL_SIZE);

        Random random = new Random();

        for (int i = 1; i <= BookingConfig.TOTAL_USERS; i++) {
            int requestedTickets = random.nextInt(BookingConfig.MAX_TICKETS_PER_REQUEST)+1;

            executor.submit(
                    new UserBookingTask(
                            inventory,
                            "User-" + i,
                            requestedTickets
                    )
            );
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("----------------------------------");
        System.out.println("Final Tickets Remaining: "
                + inventory.getAvailableTickets());
    }
}
