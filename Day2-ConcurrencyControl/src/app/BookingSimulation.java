package app;

import service.TicketInventory;
import task.UserBookingTask;

public class BookingSimulation {
    static void main() throws InterruptedException{

        TicketInventory inventory = new TicketInventory(10);

        Thread user1 = new Thread(new UserBookingTask(inventory,"User-1",7));
        Thread user2 = new Thread(new UserBookingTask(inventory,"User-2",6));
        Thread user3 = new Thread(new UserBookingTask(inventory,"User-3",3));

        user1.start();
        user2.start();
        user3.start();

        user1.join();
        user2.join();
        user3.join();

        System.out.println("----------------------------------");
        System.out.println("Final Tickets Remaining: "
                + inventory.getAvailableTickets());
    }
}
