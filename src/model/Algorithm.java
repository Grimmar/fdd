package model;

import java.io.File;
import java.util.List;
import javax.swing.event.ChangeListener;

public interface Algorithm {

    public void start();

    public void stop();

    public boolean isActive();

    public boolean isRunning();

    public boolean hasStarted();

    public boolean isStopped();

    File getFile();

    void setFile(File f);

    void closeFile();

    ChangeListener[] getChangeListeners();

    void addChangeListener(ChangeListener cl);

    void removeChangeListener(ChangeListener cl);

    List<Rule> getRules();

    double getMinSupport();

    void setMinSupport(double minSupport);
}