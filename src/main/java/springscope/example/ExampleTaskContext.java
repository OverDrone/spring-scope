package springscope.example;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import springscope.config.ScopeNames;

/**
 * Пример короткоживущего объекта.
 * Это как раз то, что у меня гуляет по callstack.
 * Хранит состояние. С данным объектом ведется работа из нескольких потоков
 * Обязательно делать прокси, иначе данный бин начнет инстанциироваться при старте приложения, а не при создании соответствующего скоупа
 */
@Component
@Scope(scopeName = ScopeNames.MAIN, proxyMode = ScopedProxyMode.INTERFACES)
public class ExampleTaskContext implements IExampleTaskContext, DisposableBean {
    private final CountDownLatch latch = new CountDownLatch(1);
    /**
     * Обычная переменная для демонстрации того, что одновременно может существовать несколько объектов контекста и не мешать друг другу
     */
    private final UUID id = UUID.randomUUID(); 
    
    public ExampleTaskContext() {
        System.out.println("ExampleTaskContext constructor. thread=" + Thread.currentThread().getName() + ",id=" + id);
    }
    
    @Override
    public void log(final String message) {
        System.out.println("ExampleTaskContext message " + message + ". thread=" + Thread.currentThread().getName() + ",id=" + id);
    }

    /**
     * Показывается что бин может определять кастомное уничтожение (spring а не jre) и оно корректно вызовется при закрытии скоупа
     */
    @Override
    public void destroy() throws Exception {
        System.out.println("ExampleTaskContext spring destroy. thread=" + Thread.currentThread().getName() + ",id=" + id);
    }
    
    @Override
    public void tick() {
        latch.countDown();
    }
    
    @Override
    public void await() throws InterruptedException {
        latch.await();
    }
}
