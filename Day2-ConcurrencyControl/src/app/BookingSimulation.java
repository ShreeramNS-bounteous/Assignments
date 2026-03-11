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

        long startTime = System.nanoTime();

        for (int i = 1; i <= BookingConfig.TOTAL_USERS; i++) {
            int requestedTickets = random.nextInt(BookingConfig.MAX_TICKETS_PER_REQUEST)+1;

            final int userId = i;

            executor.submit(
                    ()->inventory.bookTickets("User-"+userId,requestedTickets)
            );
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        long endTime = System.nanoTime();

        System.out.println("----------------------------------");
        System.out.println("Summary");
        System.out.println("----------------------------------");

        System.out.println("Initial Tickets: " + BookingConfig.INITIAL_TICKETS);
        System.out.println("Tickets Sold: " + inventory.getTicketsSold());
        System.out.println("Successful Bookings: " + inventory.getSuccessfulBookings());
        System.out.println("Failed Bookings: " + inventory.getFailedBookings());
        System.out.println("Remaining Tickets: " + inventory.getAvailableTickets());

        System.out.println("Execution Time: " + (endTime - startTime)/1_000_000 + " ms");
    }
}
