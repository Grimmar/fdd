package model;

import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public abstract class ThreadedAlgorithm implements Algorithm {

    private Thread thread;
    private volatile boolean stopped;
    private volatile boolean started;
    private transient ChangeEvent changeEvent;
    private EventListenerList listenerList;

    public ThreadedAlgorithm() {
        stopped = true;
        stopped = false;
        listenerList = new EventListenerList();
    }

    @Override
    public void start() {
        if (hasStarted()) {
            throw new IllegalMonitorStateException("isStarted");
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStopped()) {
                    if (isRunning()) {
                        try {
                            execute();
                        } catch (IOException ex) {
                            started = false;
                            stopped = true;
                        }
                    }
                }
            }
        });
        started = true;
        stopped = false;
        fireStateChanged();
        thread.start();
    }

    @Override
    public void stop() {
        if (thread == null) {
            return;
        }
        started = false;
        stopped = true;
        fireStateChanged();
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            return;
        } finally {
            thread = null;
        }
    }

    @Override
    public boolean isActive() {
        return hasStarted() && !isStopped();
    }

    @Override
    public boolean isRunning() {
        return isActive();
    }

    @Override
    public boolean hasStarted() {
        return started;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    @Override
    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) listenerList
                .getListeners(ChangeListener.class);
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
        if (cl != null) {
            listenerList.add(ChangeListener.class, cl);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
        if (cl != null) {
            listenerList.remove(ChangeListener.class, cl);
        }
    }

    protected void fireStateChanged() {
        ChangeListener[] lst = listenerList.getListeners(ChangeListener.class);
        for (ChangeListener c : lst) {
            if (changeEvent == null) {
                changeEvent = new ChangeEvent(this);
            }
            c.stateChanged(changeEvent);
        }
    }

    protected abstract void execute() throws IOException;
}
