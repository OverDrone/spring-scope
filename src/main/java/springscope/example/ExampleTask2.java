package springscope.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Пример задачи 2 уровня.
 * Работает в отдельном потоке.
 * Сигнализирует задаче 1 уровня о своем завершении
 */
@Component
public class ExampleTask2 implements IExampleTask2 {
    
    @Autowired
    private IExampleTaskContext context;
    
    @Override
    public void run() {
        context.log("ExampleTask2 run");
        context.tick();
    }
}
