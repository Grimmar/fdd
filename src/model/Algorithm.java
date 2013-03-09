package model;

import java.io.File;

public interface Algorithm {

	public void start();

	public void stop();

	public boolean isActive();

	public boolean isRunning();

	public boolean hasStarted();

	public boolean isStopped();
	
	public File getFile();
	
	public void setFile(File f);
	
	public void closeFile();
}
