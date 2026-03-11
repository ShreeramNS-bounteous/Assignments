//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() throws InterruptedException {



            AtomicInteger stock = new AtomicInteger(10);

            Runnable tradeTask = () -> {

                int request = 4;

                while (true) {

                    int currentStock = stock.get();

                    if (currentStock < request) {
                        System.out.println(Thread.currentThread().getName()
                                + " FAILED - insufficient stock");
                        return;
                    }

                    int newStock = currentStock - request;

                    if (stock.compareAndSet(currentStock, newStock)) {

                        System.out.println(Thread.currentThread().getName()
                                + " SUCCESS - bought "
                                + request + " remaining "
                                + newStock);
                        return;
                    }
                }
            };

            for(int i = 0; i < 5; i++) {
                new Thread(tradeTask).start();
            }
}
