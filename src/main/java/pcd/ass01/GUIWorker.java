package pcd.ass01;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GUIWorker extends Thread {

    private final CyclicBarrier barrierVel;
    private final CyclicBarrier barrierPos;
    private final BoidsView view;

    private static final int FRAMERATE = 1000;
    private int frameRate;

    public GUIWorker(final BoidsView view, final CyclicBarrier barrierVel, final CyclicBarrier barrierPos) {
        this.view = view;
        this.barrierVel = barrierVel;
        this.barrierPos = barrierPos;
    }

    public void run() {
        while (true) {
            final long t0 = System.currentTimeMillis();

            try {
                this.barrierVel.await();
            } catch (final InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }

            try {
                this.barrierPos.await();
            } catch (final InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }

            this.view.update(this.frameRate);
            final long t1 = System.currentTimeMillis();
            final long dtElapsed = t1 - t0;
            final int frameRatePeriod = 1000 / FRAMERATE;
            log(dtElapsed + " " + frameRatePeriod);

            if (dtElapsed < frameRatePeriod) {
                try {
                    Thread.sleep(frameRatePeriod - dtElapsed);
                } catch (Exception ex) {
                }
                this.frameRate = FRAMERATE;
            } else {
                this.frameRate = (int) (1000 / dtElapsed);
            }
        }
    }

    private void log(final String st){
        synchronized(System.out) {
            System.out.println("[" + this.getName() + "] " + st);
        }
    }

}
