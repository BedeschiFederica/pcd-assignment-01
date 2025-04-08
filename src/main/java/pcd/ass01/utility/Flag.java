package pcd.ass01.utility;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Flag {

	private boolean flag;
	private final Lock mutex;
	
	public Flag() {
		this.flag = false;
		this.mutex = new ReentrantLock();
	}

	public void reset() {
		try {
			this.mutex.lock();
			this.flag = false;
		} finally {
			this.mutex.unlock();
		}
	}

	public void set() {
		try {
			this.mutex.lock();
			this.flag = true;
		} finally {
			this.mutex.unlock();
		}
	}

	public boolean isSet() {
		try {
			this.mutex.lock();
			return this.flag;
		} finally {
			this.mutex.unlock();
		}
	}
}
