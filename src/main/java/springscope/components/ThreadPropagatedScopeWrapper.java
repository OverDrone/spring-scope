package springscope.components;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Оборачивает один экземпляр {@link ThreadPropagatedScope}.
 * Если нужно сделать несколько скоупов с разным временем жизни, на каждый такой скоуп нужно создать данный бин и вызывать его методы.
 */
public class ThreadPropagatedScopeWrapper implements IThreadPropagatedScopeWrapper, BeanFactoryPostProcessor {
    private ThreadPropagatedScope scope = new ThreadPropagatedScope();
    private String scopeName;
    
    @Required
    public void setScopeName(final String name) {
        this.scopeName = name;
    }
    
    
    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        beanFactory.registerScope(scopeName, scope);
    }
    
    /**
     * @see ThreadPropagatedScope#wrap(Runnable)
     */
    @Override
    public Runnable wrap(final Runnable runnable) {
        final Runnable result = scope.wrap(runnable);
        return result;
    }
    
    /**
     * Выполнить синхронно метод в контексте скоупа
     */
    @Override
    public void run(final Runnable runnable) {
        scope.open();
        try {
            runnable.run();
        } finally {
            scope.close();
        }
    }


}
