package pcd.ass01;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierImpl implements Barrier {

    private final int nParticipants;
    private final Lock lock;
    private final Condition ready;
    private int countArrived = 0;
    private int countRestarted;

    public BarrierImpl(final int nParticipants) {
        this.nParticipants = nParticipants;
        this.lock = new ReentrantLock();
        this.ready = this.lock.newCondition();
    }

    @Override
    public void await() throws InterruptedException {
        try {
            this.lock.lock();
            this.countArrived++;
            if (this.countArrived == this.nParticipants) {
                this.ready.signalAll();
                this.countRestarted = this.countArrived - 1;
            } else {
                while (this.countArrived != this.nParticipants) {
                    this.ready.await();
                }
                this.countRestarted--;
                if (this.countRestarted == 0) {
                    this.countArrived -= this.nParticipants;
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

}
