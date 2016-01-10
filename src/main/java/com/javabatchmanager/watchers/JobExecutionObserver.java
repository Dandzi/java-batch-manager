package com.javabatchmanager.watchers;


public interface JobExecutionObserver{
	
	void update(Object arg);
	
	void setConnected(boolean connected);
}
