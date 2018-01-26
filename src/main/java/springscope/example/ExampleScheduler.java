package springscope.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import springscope.components.IThreadPropagatedScopeWrapper;

/**
 * Пример класса, метод которого вызывается спрингом. 
 * Это корневой метод для всей мультипоточной задачи.
 * Запускает синхронно задачу 1 уровня
 */
@Component
public class ExampleScheduler {
    
    @Autowired
    private ExampleTask1 task1;
    
    @Autowired
    private IThreadPropagatedScopeWrapper scopeWrapper;
    
    @Scheduled(initialDelay = 0, fixedRate = 9999999)
    public void run1() {
        scopeWrapper.run(task1);
        //Можно вызывать подряд друг за другом
        scopeWrapper.run(task1);
    }

    @Scheduled(initialDelay = 0, fixedRate = 9999999)
    public void run2() {
        //Можно вызывать параллельно в соседнем потоке
        scopeWrapper.run(task1);
        scopeWrapper.run(task1);
    }
}
