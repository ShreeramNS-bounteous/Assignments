package service;

import java.util.concurrent.atomic.AtomicInteger;


public class TicketInventory {

    private final AtomicInteger availableTickets;


    public TicketInventory(int availableTickets){
        this.availableTickets = new AtomicInteger(availableTickets);
    }

    public void bookTickets(String userName, int requestedTickets) {

        while (true){
            int currentTickets = availableTickets.get();

            // Check if enough tickets are available
            if(currentTickets < requestedTickets){
                System.out.println(
                        userName + " failed to book "
                                + requestedTickets +
                                " tickets. Remaining: "
                                + currentTickets
                );
                return;
            }

            int newTickets = currentTickets - requestedTickets;

            // Check if enough tickets are available
            if(availableTickets.compareAndSet(currentTickets,newTickets)){

                System.out.println(
                        userName + " requested " + requestedTickets +
                                " → Booked → Remaining: " + newTickets
                );

                return;
            }

            // If CAS fails → retry loop
        }

    }
    public int getAvailableTickets(){
        return availableTickets.get();
    }
}
