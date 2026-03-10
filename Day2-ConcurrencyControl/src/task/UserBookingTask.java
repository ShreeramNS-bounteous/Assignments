package task;

import service.TicketInventory;

public class UserBookingTask implements Runnable {

    private final TicketInventory inventory;
    private final String userName;
    private final int requestedTickets;

    public UserBookingTask(TicketInventory inventory, String userName, int requestedTickets) {
        this.inventory = inventory;
        this.userName = userName;
        this.requestedTickets = requestedTickets;
    }

    @Override
    public void run() {
        inventory.bookTickets(userName,requestedTickets);
    }
}
