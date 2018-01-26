package springscope.components;

public interface IThreadPropagatedScopeWrapper {
    Runnable wrap(Runnable runnable);

    void run(Runnable runnable);
}
