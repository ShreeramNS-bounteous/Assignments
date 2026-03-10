package service;

public class TicketInventory {

    private int availableTickets;

    public TicketInventory(int availableTickets){
        this.availableTickets = availableTickets;
    }

    public void bookTickets(String userName, int requestedTickets){

        System.out.println(userName + " is trying to access");

        synchronized (this) {
            // Check if enough tickets exist
            if (availableTickets >= requestedTickets) {

                // Simulate processing delay to increase race condition chance
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                availableTickets -= requestedTickets;
                System.out.println(
                        userName + " booked " + requestedTickets +
                                " tickets. Remaining: " + availableTickets
                );
            } else {

                System.out.println(
                        userName + " failed to book " + requestedTickets +
                                " tickets. Remaining: " + availableTickets
                );
            }
        }
    }

    public int getAvailableTickets(){
        return availableTickets;
    }
}
