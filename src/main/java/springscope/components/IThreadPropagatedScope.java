package springscope.components;

interface IThreadPropagatedScope {
    void open();

    void close();

    Runnable wrap(Runnable runnable);
}
