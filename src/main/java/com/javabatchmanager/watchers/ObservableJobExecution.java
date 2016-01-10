package com.javabatchmanager.watchers;


public class ObservableJobExecution {
    private JobExecutionObserver obs;
    	
    public void addObserver(JobExecutionObserver o) {
        if (o == null)
            throw new NullPointerException();
        obs=o;
    }
    
    public void notifyObserver(Object arg){
    	obs.update(arg);
    }
}
