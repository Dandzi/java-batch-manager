package com.javabatchmanager.watchers;

import java.util.concurrent.locks.Lock;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;


@Aspect
public class LockingAspect {
	private final static Logger logger = Logger.getLogger(LockingAspect.class.getName());
	private Lock lock;
	
	@Before(value = "@within(com.javabatchmanager.watchers.Monitor) || @annotation(com.javabatchmanager.watchers.Monitor)")
	public void acquireLock(JoinPoint joinPoint){
		lock.lock();
		logger.info("Lock acquired.");
	}
	@After(value = "@within(com.javabatchmanager.watchers.Monitor) || @annotation(com.javabatchmanager.watchers.Monitor)")
	public void releaseLock(JoinPoint joinPoint){
		lock.unlock();
		logger.info("Lock released.");
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}
}
