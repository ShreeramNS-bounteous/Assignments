package service;

import java.util.concurrent.atomic.AtomicInteger;


public class TicketInventory {

    private final AtomicInteger availableTickets;

    private final AtomicInteger successfulBookings = new AtomicInteger(0);
    private final AtomicInteger failedBookings = new AtomicInteger(0);
    private final AtomicInteger ticketsSold = new AtomicInteger(0);
    private final AtomicInteger bookingSequence = new AtomicInteger(0);


    public TicketInventory(int availableTickets){
        this.availableTickets = new AtomicInteger(availableTickets);
    }

    public void bookTickets(String userName, int requestedTickets) {

        while (true){
            int currentTickets = availableTickets.get();

            // Check if enough tickets are available
            if(currentTickets < requestedTickets){

                failedBookings.incrementAndGet();

                int sequence = bookingSequence.incrementAndGet();

                System.out.println(
                        "[" + sequence + "] " +
                                userName + " requested " + requestedTickets +
                                " → Failed (Remaining: " + currentTickets + ")"
                );
                return;
            }

            int newTickets = currentTickets - requestedTickets;

            // Check if enough tickets are available
            if (availableTickets.compareAndSet(currentTickets, newTickets)) {

                int sequence = bookingSequence.incrementAndGet();

                successfulBookings.incrementAndGet();
                ticketsSold.addAndGet(requestedTickets);

                System.out.println(
                        "[" + sequence + "] " +
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

    public int getSuccessfulBookings(){
        return successfulBookings.get();
    }

    public int getFailedBookings(){
        return failedBookings.get();
    }

    public int getTicketsSold(){
        return ticketsSold.get();
    }
}
