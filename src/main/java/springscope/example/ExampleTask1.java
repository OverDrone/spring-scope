package springscope.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import springscope.components.IThreadPropagatedScopeWrapper;

/**
 * Пример задачи 1 уровня. 
 * Выполняется в корневом потоке.
 * Запускает поток задачи 2 уровня и ждет его
 */
@Component
public class ExampleTask1 implements IExampleTask1 {
    @Autowired
    private IExampleTask2 task2;
    
    @Autowired
    private IExampleTaskContext context;
    
    @Autowired
    @Qualifier("mainExecutor")
    private TaskExecutor executor;
    
    @Autowired
    private IThreadPropagatedScopeWrapper scopeWrapper;
    
    public void run() {
        context.log("ExampleTask1 run");
        final Runnable wrapped = scopeWrapper.wrap(task2); 
        executor.execute(wrapped);
        try {
            context.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
