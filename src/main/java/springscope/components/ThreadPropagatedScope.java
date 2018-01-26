package springscope.components;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import com.google.common.base.Preconditions;

class ThreadPropagatedScope implements Scope, IThreadPropagatedScope {
    private ThreadLocal<Context> contextHolder = new ThreadLocal<>();
    
    @Override
    public Object get(final String name, final ObjectFactory<?> objectFactory) {
        final Context context = contextHolder.get();
        Preconditions.checkNotNull(context);
        final Object result = context.get(name, objectFactory);
        return result;
    }

    @Override
    public Object remove(final String name) {
        final Context context = contextHolder.get();
        Preconditions.checkNotNull(context);
        final Object result = context.remove(name);
        return result;
    }

    @Override
    public void registerDestructionCallback(final String name, final Runnable callback) {
        final Context context = contextHolder.get();
        Preconditions.checkNotNull(context);
        context.registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(final String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getConversationId() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void open() {
        final Context existingContext = contextHolder.get();
        Preconditions.checkArgument(existingContext == null);
        final Context newContext = new Context();
        contextHolder.set(newContext);
    }
    
    @Override
    public void close() {
        final Context context = contextHolder.get();
        if (context != null) {
            context.close();
        }
        contextHolder.set(null);
    }
    
    /**
     * Необходимо вызывать непосредственно перед постановкой в очередь новой задачи.
     * Копирует контекст текущего потока в создаваемый
     */
    @Override
    public Runnable wrap(final Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        final Context existingContext = contextHolder.get();
        final Runnable result = () -> {
            contextHolder.set(existingContext);
            try {
                runnable.run();
            } finally {
                contextHolder.set(null);
            }
        };
        return result;
    }
    
    
    private static final class Context {
        private final Map<String, Object> objects = new ConcurrentHashMap<>();
        private final Map<String, Runnable> destructionCallbacks = new ConcurrentHashMap<>();
        
        Object get(final String name, final ObjectFactory<?> objectFactory) {
            final Function<String, Object> constructor = (nameArgument) -> {
                final Object result = objectFactory.getObject();
                return result;
            };
            final Object result = objects.computeIfAbsent(name, constructor);
            return result;
        }

        void close() {
            for (final Runnable callback : destructionCallbacks.values()) {
                callback.run();
            }
            objects.clear();
            destructionCallbacks.clear();
        }

        void registerDestructionCallback(final String name, final Runnable callback) {
            final Runnable previousValue = destructionCallbacks.put(name, callback);
            Preconditions.checkArgument(previousValue == null, "already registered %s", name);
        }

        Object remove(final String name) {
            final Runnable destructionCallback = destructionCallbacks.remove(name);
            final Object result = objects.remove(name);
            if (destructionCallback != null) {
                destructionCallback.run();
            }
            return result;
        }
    }
}
