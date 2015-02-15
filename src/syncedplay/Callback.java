package syncedplay;

public abstract class Callback implements Runnable {

    Object[] m_args;

    public Callback() {
    }

    public void run(Object... args) {
        setArgs(args);
        run();
    }

    public void setArgs(Object... args) {
        m_args = args;
    }

    public int getArgCount() {
        return m_args == null ? 0 : m_args.length;
    }

    public Object[] getArgs() {
        return m_args;
    }
}