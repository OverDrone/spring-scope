package springscope.example;

public interface IExampleTaskContext {
    void log(String message);

    void await() throws InterruptedException;

    void tick();
}
