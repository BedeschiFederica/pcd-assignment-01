package pcd.ass01;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SuspendMonitor {

    private boolean suspend;
    private final Lock mutex;
    private final Condition isSuspended;

    public SuspendMonitor() {
        this.suspend = false;
        this.mutex = new ReentrantLock();
        this.isSuspended = this.mutex.newCondition();
    }

    public void resume() {
        try {
            this.mutex.lock();
            this.suspend = false;
            this.isSuspended.signalAll();
        } finally {
            mutex.unlock();
        }
    }

    public void setSuspend() {
        try {
            this.mutex.lock();
            this.suspend = true;
        } finally {
            this.mutex.unlock();
        }
    }

    public void suspendIfRequested() {
        try {
            this.mutex.lock();
            while (this.suspend) {
                try {
                    this.isSuspended.await();
                } catch (final InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
            mutex.unlock();
        }
    }
}
