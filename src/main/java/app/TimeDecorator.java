package app;

public class TimeDecorator implements TimeForOperationsDecoratorInterface {
    private final Runnable operation;

    public TimeDecorator(Runnable operation) {
        this.operation = operation;
    }

    @Override
    public void performOperation() {
        long startTime = System.currentTimeMillis();
        operation.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");
    }
}
