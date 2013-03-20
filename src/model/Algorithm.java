package model;

import java.io.File;
import java.util.List;
import javax.swing.event.ChangeListener;

public interface Algorithm {

    void start();

    void stop();

    boolean isActive();

    boolean isRunning();

    boolean hasStarted();

    boolean isStopped();

    File getFile();

    void setFile(File f);

    void closeFile();

    ChangeListener[] getChangeListeners();

    void addChangeListener(ChangeListener cl);

    void removeChangeListener(ChangeListener cl);

    List<Rule> getRules();

    double getMinSupport();

    void setMinSupport(double minSupport);

    List<Rule> getApproximativeRules();
}